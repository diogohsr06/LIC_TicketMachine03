---------------------------------------------------------------------------------------------
-- Counter 5 Bits
---------------------------------------------------------------------------------------------

LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity Counter5 is
    port(
			--Inputs
			CE: in std_logic;									--Count Enable (Estado do contador)
			CLK: in std_logic;								--Relogio
			RESET: in std_logic;								--Clear
			
			--Outputs
			Q: out std_logic_vector(4 downto 0));		--Valor de contagem
end Counter5;

architecture arch_counter5 of Counter5 is
--Adder 5 bits
component Adder5 is
	port(
        A: in std_logic_vector(4 downto 0);
        B: in std_logic_vector(4 downto 0);
        S: out std_logic_vector(4 downto 0)
);
end component;
--Registo 5 bits
component Reg5 is
	port(
        D: in std_logic_vector(4 downto 0);
        MCLK: in std_logic;
        RESET: in std_logic;
        Q: out std_logic_vector(4 downto 0)
);
end component;
--Count Enable 5 bits
component CountEnable5 is
	port (
         A: in std_logic_vector(4 downto 0);
         B: in std_logic_vector(4 downto 0);
	      S: in std_logic;  
         Y: out std_logic_vector(4 downto 0)
);
end component;

--Sinais intermedios
signal QtoReg: std_logic_vector(4 downto 0);				--Saida do contador
signal QtoAdder: std_logic_vector(4 downto 0);			--Saida do registo/Operando A
signal AdderB: std_logic_vector(4 downto 0);				--Valor do incremento (0 ou 1)
begin
UADD: Adder5 port map(				--Instanciaçao do somador
       A => QtoAdder,
		 B => AdderB,
		 S => QtoReg);
		 
UREG: Reg5 port map(					--Instanciaçao do somador
      D => QtoReg,
		MCLK => CLK,
		RESET => RESET,
		Q => QtoAdder);
		
UCE: CountEnable5 port map(		--Instanciaçao do somador
     A => "00000",
	  B => "00001",
	  S => CE,
	  Y => AdderB);
	  
Q <= QtoAdder;
		 
end arch_counter5;