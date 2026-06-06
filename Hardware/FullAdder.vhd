---------------------------------------------------------------------------------------------
-- FullAdder
---------------------------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;

entity FullAdder is
	port(
		  A  : in std_logic;		--Operando
        B  : in std_logic;		--Operando
        Ci : in std_logic;		--Carry in
        S  : out std_logic;	--Resultado
        Co : out std_logic		--Carry out
);
end FullAdder;

architecture arch_fulladder of FullAdder is
begin

S <= A xor B xor Ci;										--Logica da soma
Co <= (A and B) or (A and Ci) or (B and Ci);		--Logica do carry out
  
end arch_fulladder;