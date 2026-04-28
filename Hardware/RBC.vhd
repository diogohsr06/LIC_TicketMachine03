library ieee;
use IEEE.std_logic_1164.all;

entity RBC is
    port(
			DAV: in std_logic;
			CTS: in std_logic;
			full: in std_logic;
			empty: in std_logic;
			
			Wr: out std_logic;
			selPG: out std_logic;
			incPut: out std_logic;
			incGet: out std_logic;
			Wreg: out std_logic;
			DAC: out std_logic);
end RBC;

architecture arch_RBC of RBC is
begin








end arch_RBC;