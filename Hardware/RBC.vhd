library ieee;
use IEEE.std_logic_1164.all;

entity RBC is
    port(
			CLK: in std_logic;
			RESET: in std_logic;
			DAV: in std_logic;
			CTS: in std_logic;
			full: in std_logic;
			empty: in std_logic;
			
			Wr: out std_logic;
			selPG: out std_logic;
			incPut: out std_logic;
			incGet: out std_logic;
			Wreg: out std_logic;
			DAC: out std_logic);
end RBC;

architecture arch_RBC of RBC is
component FlipFlop IS
PORT(	CLK : in std_logic;
		RESET : in STD_LOGIC;
		SET : in std_logic;
		D : IN STD_LOGIC;
		EN : IN STD_LOGIC;
		Q : out std_logic
		);
END component;

signal Q1, Q0, D1, D0: std_logic;

begin
FF1: FlipFlop port map (
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D1,
	  EN => '1',
	  Q => Q1);
	  
FF0: FlipFlop port map (
	  CLK => CLK,
	  RESET => RESET,
	  SET => '0',
	  D => D0,
	  EN => '1',
	  Q => Q0);

D1 <= (not (Q1) AND NOT (Q0) AND (not (DAV) and not (empty) and CTS)) or (not (Q1) and Q0) or (Q1 and not (Q0));
D0 <= (not (Q1) AND NOT (Q0) AND (not (DAV) and not (empty) and CTS)) or (not (Q1) and not (Q0) and (DAV and not(full))) or (Q1 and not (Q0) and not (DAV));

selPG <= (not (Q1) and Q0);
wr <= (not (Q1) and Q0);
incPut <= (not (Q1) and Q0);
DAC <= (Q1 and not (Q0));
Wreg <= (Q1 and Q0);
incGet <= (Q1 and Q0);

end arch_RBC;