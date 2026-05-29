library ieee;
use ieee.std_logic_1164.all;

entity RingBuffer is
    port(
        CLK   : in  std_logic;
        RESET : in  std_logic;
        DAV   : in  std_logic;
        CTS   : in  std_logic;
        Din   : in  std_logic_vector(3 downto 0);

        DAC   : out std_logic;
        Wreg  : out std_logic;
        Dout  : out std_logic_vector(3 downto 0)
    );
end RingBuffer;

architecture arch_RB of RingBuffer is

component RBC is
    port(
        CLK    : in std_logic;
        RESET  : in std_logic;
        DAV    : in std_logic;
        CTS    : in std_logic;
        full   : in std_logic;
        empty  : in std_logic;

        Wr     : out std_logic;
        PnG  : out std_logic;
        incPut : out std_logic;
        incGet : out std_logic;
        Wreg   : out std_logic;
        DAC    : out std_logic);
end component;

component MAC is
    port(
        CLK    : in  std_logic;
        RESET    : in  std_logic;
        putget : in  std_logic;
        incPut : in  std_logic;
        incGet : in  std_logic;
        S      : out std_logic_vector(3 downto 0);
        full   : out std_logic;
        empty  : out std_logic);
end component;

component RBRAM is
    generic(
        ADDRESS_WIDTH : natural := 4;
        DATA_WIDTH    : natural := 4);
    port(
         A : in  std_logic_vector(ADDRESS_WIDTH - 1 downto 0);
         wr      : in  std_logic;
         din     : in  std_logic_vector(DATA_WIDTH - 1 downto 0);
         dout    : out std_logic_vector(DATA_WIDTH - 1 downto 0));
end component;

signal Wr_s     : std_logic;
signal PnG_s  : std_logic;
signal incPut_s : std_logic;
signal incGet_s : std_logic;
signal Wreg_s   : std_logic;
signal A_s      : std_logic_vector(3 downto 0);
signal full_s   : std_logic;
signal empty_s  : std_logic;
signal Dout_ram : std_logic_vector(3 downto 0);

begin

CONTROL : RBC
    port map(
            CLK    => CLK,
            RESET  => RESET,
            DAV    => DAV,
            CTS    => CTS,
            full   => full_s,
            empty  => empty_s,
            Wr     => Wr_s,
            PnG  => PnG_s,
            incPut => incPut_s,
            incGet => incGet_s,
            Wreg   => Wreg_s,
            DAC    => DAC);

MEMORYADDRESSCONTROL : MAC
    port map(
            CLK    => CLK,
            RESET    => RESET,
            putget => PnG_s,
            incPut => incPut_s,
            incGet => incGet_s,
            S      => A_s,
            full   => full_s,
            empty  => empty_s);

MEMORY : RBRAM
    generic map(
            ADDRESS_WIDTH => 4,
            DATA_WIDTH    => 4)
    port map(
            A => A_s,
            wr      => Wr_s,
            din     => Din,
            dout    => Dout_ram);

Dout <= Dout_ram;
Wreg <= Wreg_s;

end arch_RB;