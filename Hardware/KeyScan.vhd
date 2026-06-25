---------------------------------------------------------------------------------------------
-- Key Scan
---------------------------------------------------------------------------------------------

library ieee;
use IEEE.std_logic_1164.all;

entity KeyScan is
    port(
			--Inputs
         Rows: in std_logic_vector(3 downto 0);			--Linhas do teclado
         Kscan: in std_logic;									--Estado do varrimento
			Osc: in std_logic;									--Relogio da FPGA
			RESET: in std_logic;									--Clear da maquina
			
			--Outputs
         K: out std_logic_vector(3 downto 0);			--Codigo da tecla
         Cols: out std_logic_vector(3 downto 0);		--Colunas do teclado
         Kpress: out std_logic);								--Valor logico da pressao do botao
end KeyScan;

architecture arch_KeyScan of KeyScan is
--Divisor de relogio
component CLKDIV is
generic(div: natural := 50000000);
port ( clk_in: in std_logic;
		 clk_out: out std_logic);
end component;
--Contador de 4 bits
component Counter is
    port(
         CE: in std_logic;
         CLK: in std_logic;
         RESET: in std_logic;
			Q: out std_logic_vector(3 downto 0));
end component;
--Descodificador
component Decoder is
    port(
         S: in std_logic_vector(1 downto 0);
         R: out std_logic_vector(3 downto 0));
end component;
--Multiplexer 4:2
component MUX is
    port(
         X: in std_logic_vector(3 downto 0);
         S: in std_logic_vector(1 downto 0);
         R: out std_logic);
end component;

--Sinais intermedios
signal Qout: std_logic_vector(3 downto 0);		--Contagem
signal Rout: std_logic_vector(3 downto 0);		--Colunas
signal notKpress: std_logic;							--Sinal de pressao da tecla
signal CLK_OUT: std_logic;								--Oscilador

begin
CD: CLKDIV generic map(250000) port map(			--Instanciaçao do divisor de Clk (0.01s)
	 clk_in => Osc,
	 clk_out => CLK_OUT);
UCOUNTER: Counter port map (							--Instanciaçao do contador (Percorre o teclado)
          CE => Kscan,
			 CLK => CLK_OUT,
			 RESET => RESET,
			 Q => Qout);
			 
UDEC: Decoder port map (								--Instanciaçao do descodificador (Deteta coluna)
      S(1) => Qout(3),
		S(0) => Qout(2),
		R => Rout);
		
UMUX: MUX port map (										--Instanciaçao do descodificador (Deteta linha)
      X => Rows,
		S(1) => Qout(1),
		S(0) => Qout(0),
		R => notKpress);
		
K <= Qout;													
Cols <= not Rout;
Kpress <= not notKpress;

end arch_KeyScan;