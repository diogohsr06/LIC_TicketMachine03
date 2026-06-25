---------------------------------------------------------------------------------------------
-- Adder 4 Bits
---------------------------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;

entity Adder is
	port(
		  --Inputs
		  A  : in std_logic_vector(3 downto 0);	--Operando
        B  : in std_logic_vector(3 downto 0);	--Operando
		  
		  --Outputs
        S  : out std_logic_vector(3 downto 0)	--Resultado
);
end Adder;

architecture arch_adder of Adder is
--Full Adder
component FullAdder is
	port(
		  A  : in std_logic;
        B  : in std_logic;
        Ci : in std_logic;
        S  : out std_logic;
        Co : out std_logic
);
end component;

--Sinais intermedios
signal carry : std_logic_vector(3 downto 1);		--Propagaçao de carry
signal CI: std_logic:= '0';							--Carry In (Irrelevante)
signal CO: std_logic:= '0';							--Carry Out (Irrelevante)

begin

U1: FullAdder port map (A => A(0), B => B(0), Ci => CI, S => S(0), Co => carry(1));			--Instanciaçao do full adder 1
U2: FullAdder port map (A => A(1), B => B(1), Ci => carry(1), S => S(1), Co => carry(2));	--Instanciaçao do full adder 2
U3: FullAdder port map (A => A(2), B => B(2), Ci => carry(2), S => S(2), Co => carry(3));	--Instanciaçao do full adder 3
U4: FullAdder port map (A => A(3), B => B(3), Ci => carry(3), S => S(3), Co => CO);			--Instanciaçao do full adder 4
  
end arch_adder;