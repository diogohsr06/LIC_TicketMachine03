LIBRARY ieee;
USE ieee.std_logic_1164.ALL;

ENTITY Counter3 IS
	PORT(	
		RESET : IN STD_LOGIC;
		CE, CLK: IN STD_LOGIC;
		TC: OUT STD_LOGIC;
		Q : OUT STD_LOGIC_VECTOR(2 downto 0)
	);
END Counter3;

ARCHITECTURE arq OF Counter3 IS

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

	SIGNAL Qout: STD_LOGIC_VECTOR (2 downto 0);
	SIGNAL sD: STD_LOGIC_VECTOR (2 downto 0);

BEGIN

	sD(0) <= CE xor Qout(0);
	sD(1) <= (Qout(0) and CE) xor Qout(1);
	sD(2) <= (Qout(1) and (Qout(0) and CE)) xor Qout(2);


	UFFD0: FlipFlop 
		PORT MAP(
			CLK => CLK,
			EN => CE,
			SET => '0',
			RESET => RESET,
			D => sD(0),
			Q => Qout(0)
		);
			
	UFFD1: FlipFlop 
		PORT MAP(
			CLK => CLK,
			EN => CE,
			SET => '0',
			RESET => RESET,
			D => sD(1),
			Q => Qout(1)
		);
		
	UFFD2: FlipFlop 
		PORT MAP(
			CLK => CLK,
			EN => CE,
			SET => '0',
			RESET => RESET,
			D => sD(2),
			Q => Qout(2)
		);
		

	TC <= Qout(0) AND Qout(1) AND Qout(2);
	Q	 <= Qout;
	
END arq;