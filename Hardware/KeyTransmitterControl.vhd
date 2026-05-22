library ieee;
use ieee.std_logic_1164.all;

entity KTC is
    port(
        Load     : in  std_logic;
        RESET    : in  std_logic;
        cntDone  : in  std_logic;
        TXactive : out std_logic;
        KBfree   : out std_logic
    );
end KTC;

architecture arch_KTC of KTC is
    component FlipFlop is
        port(
            CLK   : in std_logic;
            RESET : in std_logic;
            SET   : in std_logic;
            D     : in std_logic;
            EN    : in std_logic;
            Q     : out std_logic
        );
    end component;

    signal activeReg : std_logic;
    signal ffReset   : std_logic;
begin
    ffReset <= RESET or cntDone;

    FTX: FlipFlop port map(
        CLK   => Load,
        RESET => ffReset,
        SET   => Load,
        D     => '1',
        EN    => '1',
        Q     => activeReg
    );

    TXactive <= activeReg;
    KBfree <= not activeReg;
end arch_KTC;
