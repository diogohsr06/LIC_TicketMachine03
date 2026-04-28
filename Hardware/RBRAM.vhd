library ieee;
use IEEE.std_logic_1164.all;
use ieee.std_logic_arith.all;

entity RBRAM is
    generic(
        ADDRESS_WIDTH : natural := 4;
        DATA_WIDTH : natural := 4
    );
    port(
        CLK: in std_logic; 
        A: in std_logic_vector(ADDRESS_WIDTH - 1 downto 0);
        Din: in std_logic_vector(DATA_WIDTH - 1 downto 0);
        wr: in std_logic;
        Dout: out std_logic_vector(DATA_WIDTH - 1 downto 0)
    );
end RBRAM;

architecture arch_RAM of RBRAM is
    type RAM_TYPE is array (0 to 2**ADDRESS_WIDTH - 1) of std_logic_vector(DATA_WIDTH - 1 downto 0);
    signal ram : RAM_TYPE;
begin
    process(CLK)
    begin
        if rising_edge(CLK) then
            if (wr='1') then
                ram(conv_integer(unsigned(A))) <= Din;
            end if;
            Dout <= ram(conv_integer(unsigned(A)));
        end if;
    end process;
end arch_RAM;