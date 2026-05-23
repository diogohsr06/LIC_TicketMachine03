library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity Counter3 is
    port (
        CE    : in  std_logic;
        CLK   : in  std_logic;
        RESET : in  std_logic;
        TC    : out std_logic;         
        Q     : out std_logic_vector(2 downto 0)
    );
end Counter3;

architecture arch of Counter3 is
    signal cnt : integer range 0 to 6;
begin
    process(CLK, RESET)
    begin
        if RESET = '1' then
            cnt <= 0;
        elsif rising_edge(CLK) then
            if CE = '1' then
                if cnt = 6 then
                    cnt <= 0;
                else
                    cnt <= cnt + 1;
                end if;
            end if;
        end if;
    end process;

    Q  <= std_logic_vector(to_unsigned(cnt, 3));
    TC <= '1' when cnt = 6 else '0';
end arch;