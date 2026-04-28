library IEEE;
use IEEE.std_logic_1164.all;

entity MUX6 is
    port(
		   X: in std_logic_vector(5 downto 0);
		   S: in std_logic_vector(2 downto 0);
		   R: out std_logic);
end MUX6;

architecture MuxLogic6 of MUX6 is
begin

R <= ((X(0) and not S(0) and not S(1) and not S(2)) or 
	  (X(1) and not S(0) and not S(1) and S(2)) or 
	  (X(2) and not S(0) and S(1) and not S(0)) or 
	  (X(3) and not S(0) and S(1) and S(2)) or
	  (X(4) and S(0) and not S(1) and not S(0)) or
	  (X(5) and S(0) and not S(1) and S(0)));
   
end MuxLogic6;