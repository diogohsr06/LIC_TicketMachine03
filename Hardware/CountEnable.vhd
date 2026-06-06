---------------------------------------------------------------------------------------------
-- Count enable 4 bits
---------------------------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity CountEnable is
    port(
		  --Inputs
	     A: in std_logic_vector(3 downto 0);	--Operando
		  B: in std_logic_vector(3 downto 0);	--Operando
		  S: in std_logic;							--Seletor do estado de contagem
		  
		  --Outputs
		  Y: out std_logic_vector(3 downto 0)	--Operando para o somador
);
end CountEnable;

architecture arch_CE of CountEnable is
begin

--Contagem a 0: Soma com A (0000)
--Contagem a 1: Soma com B	(0001)
Y(0) <= (not S and A(0)) or (S and B(0));		
Y(1) <= (not S and A(1)) or (S and B(1));
Y(2) <= (not S and A(2)) or (S and B(2));
Y(3) <= (not S and A(3)) or (S and B(3));
	 
end arch_CE;