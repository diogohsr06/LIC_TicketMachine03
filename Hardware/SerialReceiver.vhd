library ieee;
use IEEE.std_logic_1164.all;

entity SerialReceiver is
    port
        (
			SDX: in std_logic;
			SCLK: in std_logic;
			SS: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(9 downto 0));
end SerialReceiver;

architecture arch_SER of SerialReceiver is
component ShiftRegisterPELCD is
    port
        (
			SerialIN: in std_logic;
			SCLK: in std_logic;
			SS: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(9 downto 0));
end component;
signal Q_out: std_logic_vector(9 downto 0);
component HoldRegisterPELCD is
    port
        (
			D: in std_logic_vector(9 downto 0);
			CLK: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(9 downto 0));
end component;
signal notSS: std_logic;
begin

notSS <= not SS;

SR: ShiftRegisterPELCD port map(
	 SerialIN => SDX,
	 SCLK => SCLK,
	 SS => notSS,
	 RESET => RESET,
	 Q => Q_out);
	 
HR: HoldRegisterPELCD port map(
	 D => Q_out,
	 CLK => SS,
	 RESET => RESET,
	 Q => Q);
	 
end arch_SER;