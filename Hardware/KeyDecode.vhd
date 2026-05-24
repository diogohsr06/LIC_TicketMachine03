library ieee;
use IEEE.std_logic_1164.all;

entity KeyDecode is
    port (
        Kack   : in  std_logic;
        Tdelay : in  std_logic_vector(1 downto 0);
        Rows   : in  std_logic_vector(3 downto 0);
        RESET  : in  std_logic;
        Osc    : in  std_logic;
        
        Cols   : out std_logic_vector(3 downto 0);
        K      : out std_logic_vector(3 downto 0);
        Kval   : out std_logic
    );
end KeyDecode;

architecture arch_keydecoder of KeyDecode is

component KeyScan is
    port (
        Rows   : in  std_logic_vector(3 downto 0);
        Kscan  : in  std_logic;
        Osc    : in  std_logic;
        RESET  : in  std_logic;
            
        K      : out std_logic_vector(3 downto 0);
        Cols   : out std_logic_vector(3 downto 0);
        Kpress : out std_logic
    );
end component;
component KeyControl is
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
end component;

   
component TDelay_Timer is
    port (
        Osc        : in  std_logic;
        Timer_Clr  : in  std_logic;
        Tdelay     : in  std_logic_vector(1 downto 0);
        Timer_Done : out std_logic
    );
end component;

signal kscan_out      : std_logic;
signal kpress_out     : std_logic;
signal timer_done_sig : std_logic;
signal timer_clr_sig  : std_logic;

begin
KS: KeyScan port map (
        Rows => Rows,
        Kscan => kscan_out,
        Osc => Osc,
        RESET => RESET,
        K => K,
        Cols => Cols,
        Kpress => kpress_out);
KC: KeyControl port map (
        CLK => Osc,
        RESET => RESET,
        Kack => Kack,
        Kpress => kpress_out,
        Timer_Done => timer_done_sig, 
        Kval => Kval,
        Kscan => kscan_out,
        Timer_Clr => timer_clr_sig);
TIMER: TDelay_Timer port map (
		  Osc => Osc,
        Timer_Clr => timer_clr_sig,  
        Tdelay => Tdelay,         
        Timer_Done => timer_done_sig);

end arch_keydecoder;