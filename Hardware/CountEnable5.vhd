---------------------------------------------------------------------------------------------
-- Count enable 5 bits
---------------------------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity CountEnable5 is
    port(
		  --Inputs
	     A: in std_logic_vector(4 downto 0);		--Operando
		  B: in std_logic_vector(4 downto 0);		--Operando
		  S: in std_logic;								--Seletor do estado de contagem
		  
		  --Outputs
		  Y: out std_logic_vector(4 downto 0)		--Operando para o somador
);		  
end CountEnable5;

architecture arch_CE of CountEnable5 is
begin

--Contagem a 0: Soma com A (00000)
--Contagem a 1: Soma com B	(00001)
Y(0) <= (not S and A(0)) or (S and B(0));
Y(1) <= (not S and A(1)) or (S and B(1));
Y(2) <= (not S and A(2)) or (S and B(2));
Y(3) <= (not S and A(3)) or (S and B(3));
Y(4) <= (not S and A(4)) or (S and B(4));

end arch_CE;