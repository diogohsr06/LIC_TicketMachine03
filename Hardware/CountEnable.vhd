library IEEE;
use IEEE.std_logic_1164.all;

entity CountEnable is
    port(
	     A: in std_logic_vector(3 downto 0);
		  B: in std_logic_vector(3 downto 0);
		  S: in std_logic;
		  
		  Y: out std_logic_vector(3 downto 0)
		  );
		  
end CountEnable;

architecture MuxLogic of CountEnable is
begin
    Y(0) <= (not S and A(0)) or (S and B(0));
	 Y(1) <= (not S and A(1)) or (S and B(1));
	 Y(2) <= (not S and A(2)) or (S and B(2));
	 Y(3) <= (not S and A(3)) or (S and B(3));
end MuxLogic;