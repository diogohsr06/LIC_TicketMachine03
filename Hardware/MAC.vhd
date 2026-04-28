library ieee;
use IEEE.std_logic_1164.all;

entity MAC is
    port(
			putget: in std_logic;
			incPut: in std_logic;
			incGet: in std_logic;
			
			K: out std_logic_vector(3 downto 0);
			full: out std_logic;
			empty: out std_logic);
			
end MAC;

architecture arch_MAC of MAC is
begin






end arch_MAC;