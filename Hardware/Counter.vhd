---------------------------------------------------------------------------------------------
-- Counter 4 Bits
---------------------------------------------------------------------------------------------

LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity Counter is
    port(
			--Inputs
			CE: in std_logic;									--Count Enable (Estado do contador)
			CLK: in std_logic;								--Relogio
			RESET: in std_logic;								--Clear
			
			--Outputs
			Q: out std_logic_vector(3 downto 0));		--Valor de contagem
end Counter;

architecture arch_counter of Counter is
--Somador 4 bits
component Adder is
port
(
  A  : in std_logic_vector(3 downto 0);
  B  : in std_logic_vector(3 downto 0);
  S  : out std_logic_vector(3 downto 0)
);
end component;
--Registo 4 bits
component Reg is
port
(
  D: in std_logic_vector(3 downto 0);
  MCLK: in std_logic;
  RESET: in std_logic;
  Q: out std_logic_vector(3 downto 0)
);
end component;
--Count Enable 4 bits
component CountEnable is
port 
(
  A: in std_logic_vector(3 downto 0);
  B: in std_logic_vector(3 downto 0);
  S: in std_logic;  
  Y: out std_logic_vector(3 downto 0)
);
end component;

--Sinais intermedios
signal QtoReg: std_logic_vector(3 downto 0);			--Saida do contador
signal QtoAdder: std_logic_vector(3 downto 0);		--Saida do registo/Operando A
signal AdderB: std_logic_vector(3 downto 0);			--Valor do incremento (0 ou 1)
	
begin
UADD: Adder port map(			--Instanciaçao do somador
       A => QtoAdder,
		 B => AdderB,
		 S => QtoReg);
		 
UREG: Reg port map(				--Instanciaçao do registo
      D => QtoReg,
		MCLK => CLK,
		RESET => RESET,
		Q => QtoAdder);
		
UCE: CountEnable port map( 	--Instanciaçao do Count enable
     A => "0000",
	  B => "0001",
	  S => CE,
	  Y => AdderB);
	  
Q <= QtoAdder;
		 
end arch_counter;