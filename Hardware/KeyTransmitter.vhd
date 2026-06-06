---------------------------------------------------------------------------------------------
-- Key Transmitter
---------------------------------------------------------------------------------------------

LIBRARY ieee;
USE ieee.STD_LOGIC_1164.ALL;

entity KeyTransmitter is
    port (
			 --Inputs
          CLK: in STD_LOGIC;								--Relogio da FPGA
          Load: in STD_LOGIC;                  		--Sinal para carregar keyCode
          D: in STD_LOGIC_VECTOR(3 downto 0);  		--Key code
          TXclk: in STD_LOGIC;         				--Relogio de transmissao       
          RESET : in STD_LOGIC;							--Clear da maquina
			 
			 --Outputs
          KBfree: out STD_LOGIC;             		--Livre para receber keycode  
          TXd: out STD_LOGIC                  		--Bit de transmissao da data
);
end KeyTransmitter;

architecture arch_KT of KeyTransmitter is
--Maquina de estados do key transmitter
component KTC
    port(
         Load : in STD_LOGIC;
         Tcount : in STD_LOGIC;
         clk : in STD_LOGIC;
         reset : in STD_LOGIC;
         KBfree : out STD_LOGIC;
         Ereg : out STD_LOGIC;
         Ecounter : out STD_LOGIC;
         Rcounter : out STD_LOGIC);
end component;
--Registo de 3 bits para armazenar contagem
component CounterRegister3 
    port(
         CE, CLK, RESET: in STD_LOGIC;
         TC: out STD_LOGIC;
         Q : out STD_LOGIC_VECTOR(2 downto 0));
end component;
--Registo de 4 bits para armazenar keycode  
component RegKT
    port(
         D : in STD_LOGIC_VECTOR(3 DOWNTO 0);
         E, RESET : in STD_LOGIC;
         CLK : in STD_LOGIC;
         Q : out STD_LOGIC_VECTOR(3 DOWNTO 0));
end component;
--Multiplexer 8:2
component MUX8 
    port( 
         D : in STD_LOGIC_VECTOR(7 downto 0);
         S : in STD_LOGIC_VECTOR(2 downto 0);
         Q: out STD_LOGIC);
end component;

--Sinais intermedios
signal TC_s, Ecounter_s, Rcounter_s, Ereg_s, KBfree_s: STD_LOGIC;		
signal Count_s : STD_LOGIC_VECTOR(2 downto 0);
signal Q_s : STD_LOGIC_VECTOR(3 downto 0);
signal D_mux : STD_LOGIC_VECTOR(7 downto 0);
signal Mux_Out_s : STD_LOGIC;
    
begin

KBfree <= KBfree_s;
D_mux(0) <= '0';       					--TXd vai a 0 antes do flanco de subida do TXclk 
D_mux(1) <= '1';        				-- Start bit
D_mux(2) <= Q_s(0);     				--K0
D_mux(3) <= Q_s(1);    					--K1
D_mux(4) <= Q_s(2);     				--K2
D_mux(5) <= Q_s(3);     				--K3
D_mux(6) <= '0';        				--Stop bit
D_mux(7) <= '1';        				--TXd a 1 por padrao
TXd <= Mux_Out_s when KBfree_s = '0' else '1';

UKTC: KTC port map(						--Instanciaçao da maquina de estados
       Load => Load,
       Tcount => TC_s,
       clk => CLK,
       reset => reset,
       KBfree => KBfree_s,
       Ereg => Ereg_s,
       Ecounter => Ecounter_s,
       Rcounter => Rcounter_s);
REG: RegKT port map(						--Instanciaçao do registo
     D => D,
     E => Ereg_s,
     RESET => reset,
     CLK => CLK,
     Q => Q_s);
COUNTER: CounterRegister3 port map(			--Instanciaçao do contador
         CE => Ecounter_s,
         CLK => TXclk,
         RESET => Rcounter_s,
         TC => TC_s,
         Q => Count_s);
MUX: MUX8 port map(						--Instanciaçao do Multiplexer
     D => D_mux,
     S => Count_s,
     Q => Mux_Out_s);

END arch_KT;