library ieee;
use ieee.std_logic_1164.all;

entity Decoder is
    port(
	      S: in std_logic_vector(1 downto 0);
	      R: out std_logic_vector(3 downto 0));
end Decoder;

architecture arch_decoder of Decoder is

begin
R(0)<= not(S(0)) and not(S(1));
R(1)<= (S(0)) and not(S(1));
R(2)<= not(S(0)) and (S(1));
R(3)<= (S(0)) and (S(1));


end arch_decoder;