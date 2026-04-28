library ieee;
use ieee.std_logic_1164.all;

entity Adder3 is
port
(
  A  : in std_logic_vector(2 downto 0);
  B  : in std_logic_vector(2 downto 0);
  S  : out std_logic_vector(2 downto 0)
);
end Adder3;

architecture arch_adder3 of Adder3 is
component FullAdder is
port
(
  A  : in std_logic;
  B  : in std_logic;
  Ci : in std_logic;
  S  : out std_logic;
  Co : out std_logic
);
end component;
signal carry : std_logic_vector(2 downto 1);
signal CI: std_logic:= '0';
signal CO: std_logic:= '0';
begin
  U1: FullAdder port map (A => A(0), B => B(0), Ci => CI, S => S(0), Co => carry(1));
  U2: FullAdder port map (A => A(1), B => B(1), Ci => carry(1), S => S(1), Co => carry(2));
  U3: FullAdder port map (A => A(2), B => B(2), Ci => carry(2), S => S(2), Co => CO);
end arch_adder3;