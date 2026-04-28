LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity Counter is
    port(
			CE: in std_logic;
			CLK: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(3 downto 0));
end Counter;

architecture arch_counter of Counter is
component Adder is
port
(
  A  : in std_logic_vector(3 downto 0);
  B  : in std_logic_vector(3 downto 0);
  S  : out std_logic_vector(3 downto 0)
);
end component;

component Reg is
port
(
  D: in std_logic_vector(3 downto 0);
  MCLK: in std_logic;
  RESET: in std_logic;
  Q: out std_logic_vector(3 downto 0)
);
end component;

component CountEnable is
port 
(
  A: in std_logic_vector(3 downto 0);
  B: in std_logic_vector(3 downto 0);
  S: in std_logic;  
  Y: out std_logic_vector(3 downto 0)
);
end component;

signal QtoReg: std_logic_vector(3 downto 0);
signal QtoAdder: std_logic_vector(3 downto 0);
signal AdderB: std_logic_vector(3 downto 0);
begin
UADD: Adder port map(
       A => QtoAdder,
		 B => AdderB,
		 S => QtoReg);
		 
UREG: Reg port map(
      D => QtoReg,
		MCLK => CLK,
		RESET => RESET,
		Q => QtoAdder);
		
UCE: CountEnable port map(
     A => "0000",
	  B => "0001",
	  S => CE,
	  Y => AdderB);
	  
Q <= QtoAdder;
		 
end arch_counter;