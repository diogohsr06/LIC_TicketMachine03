---------------------------------------------------------------------------------------------
-- Ring Buffer Control
---------------------------------------------------------------------------------------------

library ieee;
use IEEE.std_logic_1164.all;

entity RBC is
    port (
			 --Inputs
          CLK: in std_logic;			--Relogio da FPGA
			 RESET: in std_logic;		--Reinicia o sistema
          DAV: in std_logic;			--Data available (Existe informaçao a armazenar)
			 CTS: in std_logic;			--Clear to send (Pronto para ler)
			 full: in std_logic;			--Sinal de Ring buffer cheio
			 empty: in std_logic;		--Sinal de Ring buffer vazio
			 
			 --Outputs
          Wr:out std_logic;			--Sinal de escrita na memoria
			 PnG: out std_logic;			--Seletor de endereço
			 incPut: out std_logic;		--Enable para Incremento do ponteiro de escrita
			 incGet: out std_logic;		--Enable para Incremento do ponteiro de leitura
			 Wreg: out std_logic;		--Enable do registo (Load)
			 DAC: out std_logic			--Data accepted (Dados aceites)
);
end RBC;

architecture arch_RBC of RBC is
--Registo 1 bit
component FlipFlop is
    port(
	      CLK : in std_logic;
		   RESET : in STD_LOGIC;
		   SET : in std_logic;
		   D : IN STD_LOGIC;
		   EN : IN STD_LOGIC;
		   Q : out std_logic);
end component;


--Sinais intermedios
signal D0, D1, D2, Q0, Q1, Q2 : std_logic;	--Entradas e saidas nos flipFlops
signal W, R : std_logic; 							--Sinais para escrever e ler, respetivamente

begin
    
W <= DAV and (not full);							--Pedido de Escrita (Existe informaçao e nao esta cheio)
R <= CTS and (not empty);							--Pedido de Leitura (Esta pronto para ler e nao esta vazio)

F2: FlipFlop port map(CLK => CLK, RESET => RESET, SET => '0', D => D2, EN => '1', Q => Q2);	--Flipflop
F1: FlipFlop port map(CLK => CLK, RESET => RESET, SET => '0', D => D1, EN => '1', Q => Q1);  --FlipFlop
F0: FlipFlop port map(CLK => CLK, RESET => RESET, SET => '0', D => D0, EN => '1', Q => Q0);  --FlipFlop

--Logica do proximo estado
--7 Estados: IDLE, REQUESTPUT, WRITEMEMORY, INCREMENTPUT, ACKWRITE, REQUESTGET, INCREMENTGET - 3 bits
D2 <= (not Q2 and not Q1 and not Q0 and (not W) and R) or (not Q2 and Q1 and Q0) or (Q2 and not Q1 and not Q0 and DAV) or (Q2 and not Q1 and Q0);
D1 <= (not Q2 and not Q1 and Q0) or (not Q2 and Q1 and not Q0) or (Q2 and not Q1 and Q0);
D0 <= (not Q2 and not Q1 and not Q0 and (W or R)) or (not Q2 and Q1 and not Q0);

--Sinais de saida
Wr <= (not Q2 and Q1 and not Q0);          
PnG <= (not Q2 and not Q1 and Q0) or (not Q2 and Q1 and not Q0); 
incPut <= (not Q2 and Q1 and Q0);              
DAC <= (Q2 and not Q1 and not Q0);          
Wreg <= (Q2 and not Q1 and Q0);             
incGet <= (Q2 and Q1 and not Q0);              

end arch_RBC;