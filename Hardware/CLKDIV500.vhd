library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.numeric_std.ALL;

entity CLKDIV500 is
    generic(
        div: natural := 25000000
    );
    port ( 
        clk_in   : in  std_logic;
        clr      : in  std_logic; 
        tick_out : out std_logic  
    );
end CLKDIV500;

architecture arch_CLKDIV of CLKDIV500 is
    signal count : natural := 0; 
begin
    process(clk_in)
    begin
        if rising_edge(clk_in) then   
            if clr = '1' then
                count <= 0;
                tick_out <= '0';
            else
                if count = (div - 1) then
                    tick_out <= '1';
                    count <= 0;     
                else
                    tick_out <= '0'; 
                    count <= count + 1;
                end if;
            end if;
            
        end if;
    end process;

end arch_CLKDIV;