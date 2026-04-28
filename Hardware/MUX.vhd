library IEEE;
use IEEE.std_logic_1164.all;

entity MUX is
    port(
		   X: in std_logic_vector(3 downto 0);
		   S: in std_logic_vector(1 downto 0);
		   R: out std_logic);
end MUX;

architecture MuxLogic of MUX is
begin
R <= ((X(0) and not S(0) and not S(1)) or (X(1) and not S(0) and  S(1)) or (X(2) and  S(0) and not S(1)) or (X(3) and  S(0) and  S(1)));
   
end MuxLogic;