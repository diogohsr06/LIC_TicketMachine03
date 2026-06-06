---------------------------------------------------------------------------------------------
-- MUX 4:2
---------------------------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity MUX is
    port(
			--Inputs
		   X: in std_logic_vector(3 downto 0);		--Data
		   S: in std_logic_vector(1 downto 0);		--Seletor
			
			--Outputs
		   R: out std_logic);							--Valor associado
end MUX;

architecture MuxLogic of MUX is
begin

R <= ((X(0) and not S(1) and not S(0)) or (X(1) and not S(1) and S(0)) or (X(2) and S(1) and not S(0)) or (X(3) and S(1) and S(0)));
   
end MuxLogic;