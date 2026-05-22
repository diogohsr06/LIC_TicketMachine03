library IEEE;
use IEEE.std_logic_1164.all;

entity MUX6 is
    port(
		   X: in std_logic_vector(5 downto 0);
		   S: in std_logic_vector(2 downto 0);
		   R: out std_logic);
end MUX6;

architecture arch_mux6 of MUX6 is
begin
    with S select
        R <= X(0) when "000",
             X(1) when "001",
             X(2) when "010",
             X(3) when "011",
             X(4) when "100",
             X(5) when "101",
             '0'  when others;
end arch_mux6;
