---------------------------------------------------------------------------------------------
-- FlipFlop 
---------------------------------------------------------------------------------------------

LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY FlipFlop IS
	PORT(	
		  --Inputs
		  CLK : in std_logic;				--Relogio da FPGA
		  RESET : in STD_LOGIC;				--Data a 0
		  SET : in std_logic;				--Data a 1
		  D : IN STD_LOGIC;					--Data
		  EN : IN STD_LOGIC;					--Enable
		
		  --Outputs
		  Q : out std_logic					--Data out
);
END FlipFlop;

ARCHITECTURE arch_ffd OF FlipFlop IS
BEGIN

Q <= '0' when RESET = '1' else '1' when SET = '1' else D WHEN rising_edge(clk) and EN = '1';

END arch_ffd;