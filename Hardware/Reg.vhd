---------------------------------------------------------------------------------------------
-- Registo 4 bits
---------------------------------------------------------------------------------------------

LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity Reg is
    port(
			--Inputs
	      D: in std_logic_vector(3 downto 0);		--Valor a armazenar
			MCLK: in std_logic;							--Relogio
			RESET: in std_logic;							--Limpeza do registo
			
			--Outputs
			Q: out std_logic_vector(3 downto 0));	--Valor armazenado
end Reg;

architecture arch_register of Reg is
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

begin

U1FFD: FlipFlop port map(		--Instanciaçao do FlipFlop 1
       D => D(3),
		 EN => '1',
		 RESET => RESET,
		 SET => '0',
		 CLK => MCLK,
		 Q => Q(3));
U2FFD: FlipFlop port map(		--Instanciaçao do FlipFlop 2
       D => D(2),
		 EN => '1',
		 RESET => RESET,
		 SET => '0',
		 CLK => MCLK,
		 Q => Q(2));
U3FFD: FlipFlop port map(		--Instanciaçao do FlipFlop 3
       D => D(1),
		 EN => '1',
		 RESET => RESET,
		 SET => '0',
		 CLK => MCLK,
		 Q => Q(1));
U4FFD: FlipFlop port map(		--Instanciaçao do FlipFlop 4
       D => D(0),
		 EN => '1',
		 RESET => RESET,
		 SET => '0',
		 CLK => MCLK,
		 Q => Q(0));
		 
end arch_register;