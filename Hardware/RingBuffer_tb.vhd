library ieee;
use ieee.std_logic_1164.all;

entity RingBuffer_tb is
end RingBuffer_tb;

architecture behavior of RingBuffer_tb is
    component RingBuffer
        port(
            CLK   : in  std_logic;
            RESET : in  std_logic;
            CTS   : in  std_logic;
            DAV   : in  std_logic;
            D     : in  std_logic_vector(3 downto 0);
            Wreg  : out std_logic;
            Q     : out std_logic_vector(3 downto 0);
            DAC   : out std_logic
        );
    end component;

    signal CLK   : std_logic := '0';
    signal RESET : std_logic := '0';
    signal CTS   : std_logic := '0';
    signal DAV   : std_logic := '0';
    signal D     : std_logic_vector(3 downto 0) := (others => '0');
    signal Wreg  : std_logic;
    signal Q     : std_logic_vector(3 downto 0);
    signal DAC   : std_logic;

    constant CLK_PERIOD : time := 20 ns;
begin
    uut: RingBuffer
    port map(
        CLK   => CLK,
        RESET => RESET,
        CTS   => CTS,
        DAV   => DAV,
        D     => D,
        Wreg  => Wreg,
        Q     => Q,
        DAC   => DAC
    );

    clk_process : process
    begin
        CLK <= '0';
        wait for CLK_PERIOD / 2;
        CLK <= '1';
        wait for CLK_PERIOD / 2;
    end process;

    stim_proc : process
        procedure push_word(constant v : in std_logic_vector(3 downto 0)) is
        begin
            D <= v;
            DAV <= '1';
            wait until DAC = '1';
            wait until rising_edge(CLK);
            DAV <= '0';
            wait until rising_edge(CLK);
            assert DAC = '0'
                report "ERRO: DAC devia voltar a '0' depois de DAV descer"
                severity error;
        end procedure;

        procedure pop_expect(constant v : in std_logic_vector(3 downto 0)) is
        begin
            CTS <= '1';
            wait until Wreg = '1';
            wait for 1 ns;
            assert Q = v
                report "ERRO: FIFO fora de ordem. Valor inesperado em Q"
                severity error;
            wait until rising_edge(CLK);
            CTS <= '0';
            wait until rising_edge(CLK);
        end procedure;
    begin
        -- Reset inicial
        RESET <= '1';
        wait for 60 ns;
        RESET <= '0';
        wait until rising_edge(CLK);

        -- Escrita de 4 teclas
        push_word("0001");
        push_word("0010");
        push_word("0011");
        push_word("0100");

        -- Leitura FIFO das 4 teclas
        pop_expect("0001");
        pop_expect("0010");
        pop_expect("0011");
        pop_expect("0100");

        -- Buffer vazio: não deve haver Wreg quando CTS sobe
        CTS <= '1';
        wait for CLK_PERIOD * 4;
        assert Wreg = '0'
            report "ERRO: Wreg ativo com buffer vazio"
            severity error;
        CTS <= '0';

        report "RingBuffer_tb concluida sem erros." severity note;
        wait;
    end process;
end behavior;
