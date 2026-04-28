library ieee;
use ieee.std_logic_1164.all;

entity FullAdder is
port
(
  A  : in std_logic;
  B  : in std_logic;
  Ci : in std_logic;
  S  : out std_logic;
  Co : out std_logic
);
end FullAdder;

architecture arch_fulladder of FullAdder is
begin
  S <= A xor B xor Ci;
  Co <= (A and B) or (A and Ci) or (B and Ci);
end arch_fulladder;