---------------------------------------------------------------------------------------------
-- Register 4 bits (Key transmitter)
---------------------------------------------------------------------------------------------

LIBRARY ieee;
USE ieee.std_logic_1164.ALL;

ENTITY RegKT IS
	PORT (
			--Inputs
		   D: IN STD_LOGIC_VECTOR(3 DOWNTO 0);			--Valor a armazenar
		   E, RESET: IN STD_LOGIC;							--Enable e RESET
		   CLK: IN STD_LOGIC;								--Relogio da FPGA
			
			--Outputs
		   Q: OUT STD_LOGIC_VECTOR(3 DOWNTO 0)			--Valor armazenado
);
END RegKT;

ARCHITECTURE arch_reg OF RegKT IS
--Registo 1 bit
COMPONENT FlipFlop
		PORT(
			  CLK : IN STD_LOGIC;
			  RESET : IN STD_LOGIC;
			  D : IN STD_LOGIC;
			  SET : IN std_logic;
			  EN : IN STD_LOGIC;
			  Q : OUT STD_LOGIC
);
END COMPONENT;

BEGIN
FF1: FlipFlop PORT MAP(		--Instanciaçao do FlipFlop1
		D => D(0),
		EN => E,
		CLK => CLK,
		Q => Q(0),
		SET => '0',
		RESET => RESET);
FF2: FlipFlop PORT MAP(		--Instanciaçao do FlipFlop2
		D => D(1),
		EN => E,
		CLK => CLK,
		Q => Q(1),
		SET => '0',
		RESET => RESET);
FF3: FlipFlop PORT MAP(		--Instanciaçao do FlipFlop3
		D => D(2),
		EN => E,
		CLK => CLK,
		Q => Q(2),
		SET => '0',
		RESET => RESET);
FF4: FlipFlop PORT MAP(		--Instanciaçao do FlipFlop4
		D => D(3),
		EN => E,
		CLK => CLK,
		Q => Q(3),
		SET => '0',
		RESET => RESET);

END arch_reg;