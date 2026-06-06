---------------------------------------------------------------------------------------------
-- Registo 5 bits
---------------------------------------------------------------------------------------------

LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity Reg5 is
    port(
			--Inputs
	      D: in std_logic_vector(4 downto 0);			--Valor a armazenar
			MCLK: in std_logic;								--Relogio
			RESET: in std_logic;								--Limpeza do registo
			
			--Outputs
			Q: out std_logic_vector(4 downto 0));		--Valor armazenado
end Reg5;

architecture arch_register5 of Reg5 is
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
       D => D(4),
		 EN => '1',
		 RESET => RESET,
		 SET => '0',
		 CLK => MCLK,
		 Q => Q(4));
U2FFD: FlipFlop port map(		--Instanciaçao do FlipFlop 2
       D => D(3),
		 EN => '1',
		 RESET => RESET,
		 SET => '0',
		 CLK => MCLK,
		 Q => Q(3));
U3FFD: FlipFlop port map(		--Instanciaçao do FlipFlop 3
       D => D(2),
		 EN => '1',
		 RESET => RESET,
		 SET => '0',
		 CLK => MCLK,
		 Q => Q(2));
U4FFD: FlipFlop port map(		--Instanciaçao do FlipFlop 4
       D => D(1),
		 EN => '1',
		 RESET => RESET,
		 SET => '0',
		 CLK => MCLK,
		 Q => Q(1));
U5FFD: FlipFlop port map(		--Instanciaçao do FlipFlop 5
       D => D(0),
		 EN => '1',
		 RESET => RESET,
		 SET => '0',
		 CLK => MCLK,
		 Q => Q(0));
		 
end arch_register5;