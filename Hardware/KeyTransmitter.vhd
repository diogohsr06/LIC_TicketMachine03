library ieee;
use IEEE.std_logic_1164.all;

entity KeyTransmitter is
    port(
			Load: in std_logic;
			D: in std_logic_vector(3 downto 0);
			TXclk: in std_logic;
			RESET: in std_logic;
			
			KBfree: out std_logic;
			TXd: out std_logic);
end KeyTransmitter;

architecture arch_KT of KeyTransmitter is
component Reg is
    port(
	      D: in std_logic_vector(3 downto 0);
			MCLK: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(3 downto 0));
end component;

component Counter3 is
    port(
			CE: in std_logic;
			CLK: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(2 downto 0));
end component;

component MUX6 is
    port(
		   X: in std_logic_vector(5 downto 0);
		   S: in std_logic_vector(2 downto 0);
		   R: out std_logic);
end component;

signal counterOut: std_logic_vector(2 downto 0);
signal Q_out: std_logic_vector(3 downto 0);

begin

UREG: Reg port map (
		D => D,
		MCLK => Load,
		RESET => RESET,
		Q => Q_out);
		
UMUX: MUX6 port map (
		X(0) => '1',
		X(1) => D(0),
		X(2) => D(1),
		X(3) => D(2),
		X(4) => D(3),
		X(5) => '0',
		S => counterOut,
		R => TXd);
		
UCOUNTER: Counter3 port map (
			 CE => '1',
			 CLK => TXclk,
			 RESET => RESET,
			 Q => counterOut);
			 
KBfree <= '1' when counterOut = "000" else '0';

end arch_KT;