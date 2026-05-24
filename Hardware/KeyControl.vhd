library ieee;
use IEEE.std_logic_1164.all;

entity KeyControl is
    port (
         CLK        : in  std_logic;
         RESET      : in  std_logic;
         Kack       : in  std_logic;
         Kpress     : in  std_logic;
         Timer_Done : in  std_logic;  
         
         Kval       : out std_logic;
         Kscan      : out std_logic;
         Timer_Clr  : out std_logic   
    );
end KeyControl;

architecture arch_keycontrol of KeyControl is
component FlipFlop IS
    PORT( CLK   : in  std_logic;
          RESET : in  STD_LOGIC;
          SET   : in  std_logic;
          D     : IN  STD_LOGIC;
          EN    : IN  STD_LOGIC;
          Q     : out std_logic
    );
END component;

signal D0, D1, Q0, Q1: std_logic;
    
begin

F1: FlipFlop port map(CLK => CLK, RESET => RESET, SET => '0', D => D1, EN => '1', Q => Q1);
F2: FlipFlop port map(CLK => CLK, RESET => RESET, SET => '0', D => D0, EN => '1', Q => Q0);
    
D1 <= (Kack and Q0 and not Q1) or (not Q0 and Q1) or (Q0 and Q1 and Kpress and not Timer_Done);
D0 <= ((Q0 xnor Q1) and Kpress) or (not Kack and (Q0 xor Q1));

Kval  <= not Q1 and Q0;
Kscan <= not Q1 and not Q0;
Timer_Clr <= not (Q1 and Q0);

end arch_keycontrol;