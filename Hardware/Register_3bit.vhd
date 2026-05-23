library ieee;
use ieee.std_logic_1164.all;

entity Register_3bit is
  port(
    CLK   : in  std_logic;
    Rst   : in  std_logic;
    EN    : in  std_logic;
    D_IN  : in  std_logic_vector(2 downto 0);
    Q_OUT : out std_logic_vector(2 downto 0)
  );
end Register_3bit;

ARCHITECTURE structural OF Register_3bit IS

    COMPONENT FlipFlop IS
        PORT(
            CLK   : in  STD_LOGIC;
            RESET : in  STD_LOGIC;
            SET   : in  STD_LOGIC;
            D     : in  STD_LOGIC;
            EN    : in  STD_LOGIC;
            Q     : out STD_LOGIC
        );
    END COMPONENT;

BEGIN

    FF0 : FlipFlop
        PORT MAP (
            CLK   => CLK,
            RESET => Rst,
            SET   => '0',
            EN    => EN,
            D     => D_IN(0),
            Q     => Q_OUT(0)
        );

    FF1 : FlipFlop
        PORT MAP (
            CLK   => CLK,
            RESET => Rst,
            SET   => '0',
            EN    => EN,
            D     => D_IN(1),
            Q     => Q_OUT(1)
        );

    FF2 : FlipFlop
        PORT MAP (
            CLK   => CLK,
            RESET => Rst,
            SET   => '0',
            EN    => EN,
            D     => D_IN(2),
            Q     => Q_OUT(2)
        );


END structural;