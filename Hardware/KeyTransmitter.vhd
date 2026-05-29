library ieee;
use ieee.std_logic_1164.all;

entity KeyTransmitter is
    port(
        Load   : in std_logic;
        D      : in std_logic_vector(3 downto 0);
        TXclk  : in std_logic;
        RESET  : in std_logic;
        KBfree : out std_logic;
        TXd    : out std_logic
    );
end KeyTransmitter;

architecture arch_KT of KeyTransmitter is
    component Reg is
        port(
            D     : in std_logic_vector(3 downto 0);
            MCLK  : in std_logic;
            RESET : in std_logic;
            Q     : out std_logic_vector(3 downto 0)
        );
    end component;

    component Counter3 is
        port(
            CE    : in std_logic;
            CLK   : in std_logic;
            RESET : in std_logic;
            TC    : out std_logic;
            Q     : out std_logic_vector(2 downto 0)
        );
    end component;

    component MUX6 is
        port(
            X : in std_logic_vector(5 downto 0);
            S : in std_logic_vector(2 downto 0);
            R : out std_logic
        );
    end component;

    component KTC is
        port(
            Load     : in  std_logic;
            RESET    : in  std_logic;
            cntDone  : in  std_logic;
            TXactive : out std_logic;
            KBfree   : out std_logic
        );
    end component;

    signal dataQ        : std_logic_vector(3 downto 0);
    signal counterQ     : std_logic_vector(2 downto 0);
    signal muxOut       : std_logic;
    signal txActive_s   : std_logic;
    signal cntDone_s    : std_logic;
    signal counterReset : std_logic;
    signal txclk_n      : std_logic;
begin
    UREG: Reg port map(
        D     => D,
        MCLK  => Load,
        RESET => RESET,
        Q     => dataQ
    );

    UKTC: KTC port map(
        Load     => Load,
        RESET    => RESET,
        cntDone  => cntDone_s,
        TXactive => txActive_s,
        KBfree   => KBfree
    );
    
    counterReset <= RESET or (not txActive_s);
    txclk_n <= not TXclk;
    
    UCOUNTER: Counter3 port map(
        CE    => '1',
        CLK   => txclk_n,
        RESET => counterReset,
        TC    => cntDone_s,
        Q     => counterQ
    );
    UMUX: MUX6 port map(
        X(0) => '1',        
        X(1) => dataQ(0),   
        X(2) => dataQ(1),  
        X(3) => dataQ(2),   
        X(4) => dataQ(3),   
        X(5) => '0',        
        S    => counterQ,
        R    => muxOut
    );
    TXd <= muxOut when txActive_s = '1' else '1';
    
end arch_KT;