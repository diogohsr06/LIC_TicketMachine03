library ieee;
use IEEE.std_logic_1164.all;

entity RBRAM is
    port(
			Din: in std_logic_vector(3 downto 0);
			wr: in std_logic;
			A: in std_logic_vector(3 downto 0);
			
			Dout: out std_logic_vector(3 downto 0));
end RBRAM;