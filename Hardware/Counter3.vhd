LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity Counter3 is
    port(
			CE: in std_logic;
			CLK: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(2 downto 0));
end Counter3;

architecture arch_counter3 of Counter3 is
component Adder3 is
port
(
  A  : in std_logic_vector(2 downto 0);
  B  : in std_logic_vector(2 downto 0);
  S  : out std_logic_vector(2 downto 0)
);
end component;

component Reg3 is
port
(
  D: in std_logic_vector(2 downto 0);
  MCLK: in std_logic;
  RESET: in std_logic;
  Q: out std_logic_vector(2 downto 0)
);
end component;

component CountEnable3 is
port 
(
  A: in std_logic_vector(2 downto 0);
  B: in std_logic_vector(2 downto 0);
  S: in std_logic;  
  Y: out std_logic_vector(2 downto 0)
);
end component;

signal QtoReg: std_logic_vector(2 downto 0);
signal QtoAdder: std_logic_vector(2 downto 0);
signal AdderB: std_logic_vector(2 downto 0);
begin
UADD: Adder3 port map(
       A => QtoAdder,
		 B => AdderB,
		 S => QtoReg);
		 
UREG: Reg3 port map(
      D => QtoReg,
		MCLK => CLK,
		RESET => RESET,
		Q => QtoAdder);
		
UCE: CountEnable3 port map(
     A => "000",
	  B => "001",
	  S => CE,
	  Y => AdderB);
	  
Q <= QtoAdder;
		 
end arch_counter3;