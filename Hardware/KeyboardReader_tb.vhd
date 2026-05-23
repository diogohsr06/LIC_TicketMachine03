library ieee;
use ieee.std_logic_1164.all;

entity KeyboardReader_tb is
end KeyboardReader_tb;

architecture behavior of KeyboardReader_tb is
    component KeyboardReader
        port(
            Rows  : in  std_logic_vector(3 downto 0);
            RESET : in  std_logic;
            Osc   : in  std_logic;
            TXclk : in  std_logic;
            TXd   : out std_logic;
            Cols  : out std_logic_vector(3 downto 0)
        );
    end component;

    signal Rows  : std_logic_vector(3 downto 0) := (others => '1'); -- pull-up
    signal RESET : std_logic := '0';
    signal Osc   : std_logic := '0';
    signal TXclk : std_logic := '0';
    signal TXd   : std_logic;
    signal Cols  : std_logic_vector(3 downto 0);

    signal key_pressed : std_logic := '0';
    signal press_row   : integer range 0 to 3 := 0;
    signal press_col   : integer range 0 to 3 := 0;

    constant OSC_PERIOD   : time := 20 ns;   -- 50 MHz
    constant TXCLK_PERIOD : time := 400 ns;  -- clock do receiver SW
begin
    uut: KeyboardReader
    port map(
        Rows  => Rows,
        RESET => RESET,
        Osc   => Osc,
        TXclk => TXclk,
        TXd   => TXd,
        Cols  => Cols
    );

    osc_process : process
    begin
        Osc <= '0';
        wait for OSC_PERIOD/2;
        Osc <= '1';
        wait for OSC_PERIOD/2;
    end process;

    txclk_process : process
    begin
        TXclk <= '0';
        wait for TXCLK_PERIOD/2;
        TXclk <= '1';
        wait for TXCLK_PERIOD/2;
    end process;

    -- Modelo do teclado matricial:
    -- colunas são ativas a '0'; linha fica a '0' apenas quando a tecla está premida
    -- e a coluna dessa tecla está a ser varrida.
    key_matrix_model : process(key_pressed, press_row, press_col, Cols)
        variable rows_v : std_logic_vector(3 downto 0);
    begin
        rows_v := (others => '1');
        if key_pressed = '1' then
            if Cols(press_col) = '0' then
                rows_v(press_row) := '0';
            end if;
        end if;
        Rows <= rows_v;
    end process;

    stim_proc : process
        procedure expect_key_code(constant expected : in integer; constant tag : in string) is
            variable sampled : std_logic_vector(5 downto 0);
            variable stop_b  : std_logic;
            variable data_i  : integer;
            variable code_i  : integer;
            variable found   : boolean;
        begin
            found := false;

            -- Espera por início de frame (TXd=0 num flanco de subida de TXclk)
            for n in 0 to 300 loop
                wait until rising_edge(TXclk);
                if TXd = '0' then
                    sampled(0) := '0';
                    found := true;
                    exit;
                end if;
            end loop;

            assert found report "ERRO " & tag & ": timeout a espera de start bit" severity error;

            for i in 1 to 5 loop
                wait until rising_edge(TXclk);
                sampled(i) := TXd;
            end loop;

            wait until rising_edge(TXclk);
            stop_b := TXd;

            assert stop_b = '1'
                report "ERRO " & tag & ": stop bit invalido"
                severity error;

            -- Decodificacao identica ao KeyReceiver.kt:
            -- data = bits[0..5], code = (data >> 1) & 0xF
            data_i := 0;
            for i in 0 to 5 loop
                if sampled(i) = '1' then
                    data_i := data_i + (2 ** i);
                end if;
            end loop;
            code_i := (data_i / 2) mod 16;

            assert code_i = expected
                report "ERRO " & tag & ": codigo recebido diferente do esperado"
                severity error;
        end procedure;
    begin
        -- Reset
        RESET <= '1';
        key_pressed <= '0';
        wait for 200 ns;
        RESET <= '0';
        wait for 500 ns;

        -- Tecla '5' => col=1,row=1 => code 5
        press_col <= 1;
        press_row <= 1;
        key_pressed <= '1';
        expect_key_code(5, "tecla 5");
        key_pressed <= '0';
        wait for 2 us;

        -- Tecla 'C' => col=3,row=2 => code 14
        press_col <= 3;
        press_row <= 2;
        key_pressed <= '1';
        expect_key_code(14, "tecla C");
        key_pressed <= '0';
        wait for 2 us;

        -- Tecla '1' => col=0,row=0 => code 0
        press_col <= 0;
        press_row <= 0;
        key_pressed <= '1';
        expect_key_code(0, "tecla 1");
        key_pressed <= '0';
        wait for 2 us;

        report "KeyboardReader_tb concluida sem erros." severity note;
        wait;
    end process;
end behavior;
