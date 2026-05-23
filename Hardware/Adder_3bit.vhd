library ieee;
use ieee.std_logic_1164.all;

entity Adder_3bit is
  port(
    A  : in  std_logic_vector(2 downto 0);
    B  : in  std_logic_vector(2 downto 0);
    Ci : in  std_logic;
    S  : out std_logic_vector(2 downto 0);
    Co : out std_logic
  );
end Adder_3bit;

architecture logic of Adder_3bit is
  signal c1, c2 : std_logic;
begin

  S(0) <= A(0) xor B(0) xor Ci;
  c1   <= (A(0) and B(0)) or (A(0) and Ci) or (B(0) and Ci);

  S(1) <= A(1) xor B(1) xor c1;
  c2   <= (A(1) and B(1)) or (A(1) and c1) or (B(1) and c1);

  S(2) <= A(2) xor B(2) xor c2;
  Co   <= (A(2) and B(2)) or (A(2) and c2) or (B(2) and c2);

end logic;