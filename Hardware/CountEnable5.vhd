library IEEE;
use IEEE.std_logic_1164.all;

entity CountEnable5 is
    port(
	     A: in std_logic_vector(4 downto 0);
		  B: in std_logic_vector(4 downto 0);
		  S: in std_logic;
		  
		  Y: out std_logic_vector(4 downto 0)
		  );
		  
end CountEnable5;

architecture MuxLogic5 of CountEnable5 is
begin
    Y(0) <= (not S and A(0)) or (S and B(0));
	 Y(1) <= (not S and A(1)) or (S and B(1));
	 Y(2) <= (not S and A(2)) or (S and B(2));
	 Y(3) <= (not S and A(3)) or (S and B(3));
	 Y(4) <= (not S and A(4)) or (S and B(4));
end MuxLogic5;