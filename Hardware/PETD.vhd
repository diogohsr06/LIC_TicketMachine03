library ieee;
use IEEE.std_logic_1164.all;

entity PETD is
    port
        (
			SDX: in std_logic;
			SCLK: in std_logic;
			SS: in std_logic;
			RESET: in std_logic;
			D9: out std_logic;
			D: out std_logic_vector(8 downto 0));
end PETD;

architecture arch_PETD of PETD is
component SerialReceiver is
    port
        (
			SDX: in std_logic;
			SCLK: in std_logic;
			SS: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(9 downto 0));
end component;

begin

USR: SerialReceiver port map(
	  SDX => SDX,
	  SCLK => SCLK,
	  SS => SS,
	  RESET => RESET,
	  Q(9) => D9,
	  Q(8) => D(8),
	  Q(7) => D(7),
	  Q(6) => D(6),
	  Q(5) => D(5),
	  Q(4) => D(4),
	  Q(3) => D(3),
	  Q(2) => D(2),
	  Q(1) => D(1),
	  Q(0) => D(0));  
	  
end arch_PETD;
	  
	  