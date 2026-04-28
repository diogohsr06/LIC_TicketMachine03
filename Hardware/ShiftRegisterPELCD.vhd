library ieee;
use IEEE.std_logic_1164.all;

entity ShiftRegisterPELCD is
    port
        (
			SerialIN: in std_logic;
			SCLK: in std_logic;
			SS: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(9 downto 0));
end ShiftRegisterPELCD;

architecture arch_SR of ShiftRegisterPELCD is
component FlipFlop IS
PORT(	CLK : in std_logic;
		RESET : in STD_LOGIC;
		SET : in std_logic;
		D : IN STD_LOGIC;
		EN : IN STD_LOGIC;
		Q : out std_logic
		);
END component;
signal Data: std_logic_vector(9 downto 0);
begin
FF1: FlipFlop port map(
	  CLK => SCLK,
	  RESET => RESET,
	  SET => '0',
	  D => SerialIN,
	  EN => SS,
	  Q => Data(9));
	  
FF2: FlipFlop port map(
	  CLK => SCLK,
	  RESET => RESET,
	  SET => '0',
	  D => Data(9),
	  EN => SS,
	  Q => Data(8));
	  
FF3: FlipFlop port map(
	  CLK => SCLK,
	  RESET => RESET,
	  SET => '0',
	  D => Data(8),
	  EN => SS,
	  Q => Data(7));
	  
FF4: FlipFlop port map(
	  CLK => SCLK,
	  RESET => RESET,
	  SET => '0',
	  D => Data(7),
	  EN => SS,
	  Q => Data(6));
	  
FF5: FlipFlop port map(
	  CLK => SCLK,
	  RESET => RESET,
	  SET => '0',
	  D => Data(6),
	  EN => SS,
	  Q => Data(5));
	  
FF6: FlipFlop port map(
	  CLK => SCLK,
	  RESET => RESET,
	  SET => '0',
	  D => Data(5),
	  EN => SS,
	  Q => Data(4));
	  
FF7: FlipFlop port map(
	  CLK => SCLK,
	  RESET => RESET,
	  SET => '0',
	  D => Data(4),
	  EN => SS,
	  Q => Data(3));
	  
FF8: FlipFlop port map(
	  CLK => SCLK,
	  RESET => RESET,
	  SET => '0',
	  D => Data(3),
	  EN => SS,
	  Q => Data(2));
	  
FF9: FlipFlop port map(
	  CLK => SCLK,
	  RESET => RESET,
	  SET => '0',
	  D => Data(2),
	  EN => SS,
	  Q => Data(1));
	  
FF10: FlipFlop port map(
	  CLK => SCLK,
	  RESET => RESET,
	  SET => '0',
	  D => Data(1),
	  EN => SS,
	  Q => Data(0));
	  
Q <= Data;

end arch_SR;
