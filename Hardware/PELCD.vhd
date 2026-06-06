---------------------------------------------------------------------------------------------
-- Port Expander LCD
---------------------------------------------------------------------------------------------

library ieee;
use IEEE.std_logic_1164.all;

entity PELCD is
    port
        (
			--Inputs
			SDX: in std_logic;							--Serial Data in
			SCLK: in std_logic;							--Relogio
			SS: in std_logic;								--Enable
			RESET: in std_logic;							--Reinicia o sistema
			
			--Outputs
			D9: out std_logic;							--MSB Data out
			D: out std_logic_vector(8 downto 0));	--Data out
end PELCD;

architecture arch_PELCD of PELCD is
--Serial Receiver
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

USR: SerialReceiver port map(		--Instanciaçao do Serial Receiver
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
	  
end arch_PELCD;
	  
	  