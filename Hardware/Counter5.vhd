LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity Counter5 is
    port(
			CE: in std_logic;
			CLK: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(4 downto 0));
end Counter5;

architecture arch_counter5 of Counter5 is
component Adder5 is
port
(
  A  : in std_logic_vector(4 downto 0);
  B  : in std_logic_vector(4 downto 0);
  S  : out std_logic_vector(4 downto 0)
);
end component;

component Reg5 is
port
(
  D: in std_logic_vector(4 downto 0);
  MCLK: in std_logic;
  RESET: in std_logic;
  Q: out std_logic_vector(4 downto 0)
);
end component;

component CountEnable5 is
port 
(
  A: in std_logic_vector(4 downto 0);
  B: in std_logic_vector(4 downto 0);
  S: in std_logic;  
  Y: out std_logic_vector(4 downto 0)
);
end component;

signal QtoReg: std_logic_vector(4 downto 0);
signal QtoAdder: std_logic_vector(4 downto 0);
signal AdderB: std_logic_vector(4 downto 0);
begin
UADD: Adder5 port map(
       A => QtoAdder,
		 B => AdderB,
		 S => QtoReg);
		 
UREG: Reg5 port map(
      D => QtoReg,
		MCLK => CLK,
		RESET => RESET,
		Q => QtoAdder);
		
UCE: CountEnable5 port map(
     A => "00000",
	  B => "00001",
	  S => CE,
	  Y => AdderB);
	  
Q <= QtoAdder;
		 
end arch_counter5;