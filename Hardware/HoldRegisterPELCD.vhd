library ieee;
use IEEE.std_logic_1164.all;

entity HoldRegisterPELCD is
    port
        (
			D: in std_logic_vector(9 downto 0);
			CLK: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(9 downto 0));
end HoldRegisterPELCD;

architecture arch_HR of HoldRegisterPELCD is
component FlipFlop IS
PORT(	CLK : in std_logic;
		RESET : in STD_LOGIC;
		SET : in std_logic;
		D : IN STD_LOGIC;
		EN : IN STD_LOGIC;
		Q : out std_logic
		);
END component;


begin

FF1: FlipFlop port map(
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D(9),
	  EN => '1',
	  Q => Q(9));
	  
FF2: FlipFlop port map(
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D(8),
	  EN => '1',
	  Q => Q(8));
	  
FF3: FlipFlop port map(
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D(7),
	  EN => '1',
	  Q => Q(7));
	  
FF4: FlipFlop port map(
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D(6),
	  EN => '1',
	  Q => Q(6));
	  
FF5: FlipFlop port map(
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D(5),
	  EN => '1',
	  Q => Q(5));
	  
FF6: FlipFlop port map(
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D(4),
	  EN => '1',
	  Q => Q(4));
	  
FF7: FlipFlop port map(
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D(3),
	  EN => '1',
	  Q => Q(3));
	  
FF8: FlipFlop port map(
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D(2),
	  EN => '1',
	  Q => Q(2));
	  
FF9: FlipFlop port map(
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D(1),
	  EN => '1',
	  Q => Q(1));
	  
FF10: FlipFlop port map(
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D(0),
	  EN => '1',
	  Q => Q(0));
	  
end arch_HR;