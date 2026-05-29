library ieee;
use ieee.std_logic_1164.all;

entity MAC is
    port(
        CLK    : in  std_logic;
        RESET    : in  std_logic;
        putget : in  std_logic;
        incPut : in  std_logic;
        incGet : in  std_logic;
        S      : out std_logic_vector(3 downto 0);
        full   : out std_logic;
        empty  : out std_logic
    );
end MAC;

architecture arch_MAC of MAC is

component Counter5 is 
    port(
        CLK : in std_logic;
        CE  : in std_logic;
        RESET : in std_logic;
        Q   : out std_logic_vector(4 downto 0)
    );
end component;

signal Addr_put : std_logic_vector(4 downto 0);
signal Addr_get : std_logic_vector(4 downto 0);

begin

INCREMENTPUT : Counter5
    port map(
        CLK => CLK,
        CE  => incPut,
        RESET => RESET,
        Q   => Addr_put);

INCREMENTGET : Counter5
    port map(
        CLK => CLK,
        CE  => incGet,
        RESET => RESET,
        Q   => Addr_get);

S <= Addr_get(3 downto 0) when putget = '0' else Addr_put(3 downto 0);

full <= '1' when (Addr_put(3 downto 0) = Addr_get(3 downto 0)) and (Addr_put(4) /= Addr_get(4)) else '0';

empty <= '1' when (Addr_put(3 downto 0) = Addr_get(3 downto 0)) and (Addr_put(4) = Addr_get(4)) else '0';

end arch_MAC;