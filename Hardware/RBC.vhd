library ieee;
use IEEE.std_logic_1164.all;

entity RBC is
    port (
        CLK, RESET : in std_logic;
        DAV, CTS, full, empty : in std_logic;
        Wr, PnG, incPut, incGet, Wreg, DAC : out std_logic
    );
end RBC;

architecture arch_RBC of RBC is
    component FlipFlop IS
        PORT( CLK, RESET, SET, D, EN : in std_logic; Q : out std_logic );
END component;

signal D0, D1, D2, Q0, Q1, Q2 : std_logic;
signal S, R : std_logic; 

begin
    
S <= DAV and (not full);
R <= CTS and (not empty);

F2: FlipFlop port map(CLK => CLK, RESET => RESET, SET => '0', D => D2, EN => '1', Q => Q2);
F1: FlipFlop port map(CLK => CLK, RESET => RESET, SET => '0', D => D1, EN => '1', Q => Q1);
F0: FlipFlop port map(CLK => CLK, RESET => RESET, SET => '0', D => D0, EN => '1', Q => Q0);

D2 <= (not Q2 and Q1 and Q0) or (not Q2 and not Q1 and not Q0 and R) or (Q2 and not Q1 and not Q0 and DAV);
D1 <= (not Q2 and not Q1 and Q0) or (not Q2 and Q1 and not Q0) or (Q2 and not Q1 and Q0);
D0 <= (not Q2 and not Q1 and not Q0 and S) or (not Q2 and Q1 and not Q0) or (Q2 and not Q1 and not Q0 and DAV);

    
Wr     <= (not Q2 and Q1 and not Q0);          
PnG    <= (not Q2 and not Q1 and Q0) or (not Q2 and Q1 and not Q0); 
incPut <= (not Q2 and Q1 and Q0);              
DAC    <= (Q2 and not Q1 and not Q0);          
Wreg   <= (Q2 and not Q1 and Q0);             
incGet <= (Q2 and Q1 and not Q0);              

end arch_RBC;