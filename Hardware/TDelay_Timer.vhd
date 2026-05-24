library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity TDelay_Timer is
    port (
        Osc        : in  std_logic;                    
        Timer_Clr  : in  std_logic;                   
        Tdelay     : in  std_logic_vector(1 downto 0);
        Timer_Done : out std_logic                    
    );
end TDelay_Timer;

architecture Structural of TDelay_Timer is
component CLKDIV500 is
    generic( div: natural := 25000000 );
    port ( 
        clk_in   : in  std_logic;
        clr      : in  std_logic;
        tick_out : out std_logic
    );
end component;
component Counter is
    port(
         CE    : in  std_logic;
         CLK   : in  std_logic;
         RESET : in  std_logic;
         Q     : out std_logic_vector(3 downto 0)
    );
end component;

signal tick_500ms : std_logic;
signal count_out  : std_logic_vector(3 downto 0);

begin
UTICKGEN: CLKDIV500 
        generic map ( div => 25000000 ) 
        port map (
            clk_in   => Osc,
            clr      => Timer_Clr,
            tick_out => tick_500ms);

    
UCOUNTER: Counter 
        port map (
            CE    => tick_500ms, 
            CLK   => Osc,
            RESET => Timer_Clr,  
            Q     => count_out);

process(count_out, Tdelay)
begin
    case Tdelay is
        when "00" =>   
            if count_out = "0001" then Timer_Done <= '1'; else Timer_Done <= '0'; end if;
        when "01" =>   
            if count_out = "0010" then Timer_Done <= '1'; else Timer_Done <= '0'; end if;
        when "10" =>  
            if count_out = "0011" then Timer_Done <= '1'; else Timer_Done <= '0'; end if;
        when others => 
            if count_out = "0100" then Timer_Done <= '1'; else Timer_Done <= '0'; end if;
    end case;
end process;

end Structural;