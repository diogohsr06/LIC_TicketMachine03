library ieee;
use ieee.std_logic_1164.all;

entity KeyTransmitter_tb is
end KeyTransmitter_tb;

architecture behavior of KeyTransmitter_tb is

    -- 1. Declaração do Componente a ser testado (UUT - Unit Under Test)
    component KeyTransmitter is
        port(
            Load   : in std_logic;
            D      : in std_logic_vector(3 downto 0);
            TXclk  : in std_logic;
            RESET  : in std_logic;
            KBfree : out std_logic;
            TXd    : out std_logic
        );
    end component;

    -- 2. Sinais internos para ligar à UUT
    signal sig_Load   : std_logic := '0';
    signal sig_D      : std_logic_vector(3 downto 0) := (others => '0');
    signal sig_TXclk  : std_logic := '0';
    signal sig_RESET  : std_logic := '0';
    signal sig_KBfree : std_logic;
    signal sig_TXd    : std_logic;

    -- 3. Definição do período do relógio de transmissão (ex: 100 ns)
    constant TXCLK_PERIOD : time := 100 ns;

begin

    -- Instanciação da Unidade Sob Teste (UUT)
    uut: KeyTransmitter port map (
          Load   => sig_Load,
          D      => sig_D,
          TXclk  => sig_TXclk,
          RESET  => sig_RESET,
          KBfree => sig_KBfree,
          TXd    => sig_TXd
        );

    -- Processo para gerar o relógio TXclk continuamente
    tx_clk_process : process
    begin
        sig_TXclk <= '0';
        wait for TXCLK_PERIOD/2;
        sig_TXclk <= '1';
        wait for TXCLK_PERIOD/2;
    end process;

    -- Processo de Estímulos (onde testamos os cenários)
    stim_proc: process
    begin		
        -- STEP 1: Estado Inicial e Reset do Sistema
        sig_RESET <= '1';
        sig_Load  <= '0';
        sig_D     <= "0000";
        wait for 200 ns; -- Mantém o reset ativo por 2 ciclos
        
        sig_RESET <= '0'; -- Liberta o reset
        wait for 50 ns;

        -- STEP 2: Testar a Transmissão da Tecla "1010" (Hex: A)
        -- Queremos ver na linha TXd: Start(0) -> K0(0) -> K1(1) -> K2(0) -> K3(1) -> Stop(1)
        sig_D    <= "1010"; 
        wait for 20 ns;
        sig_Load <= '1';     -- Dá ordem de carga (Flanco ascendente)
        wait for 40 ns;     -- Mantém o pulso de load por um momento
        sig_Load <= '0';     -- Retira a ordem de carga
        
        -- Espera que a transmissão termine. 
        -- Cada transmissão demora 6 ciclos de TXclk (Start + 4 bits + Stop) = 600 ns
        wait until sig_KBfree = '1'; 
        wait for 200 ns;     -- Intervalo de descanso em repouso

        -- STEP 3: Testar a Transmissão da Tecla "0111" (Hex: 7)
        -- Queremos ver na linha TXd: Start(0) -> K0(1) -> K1(1) -> K2(1) -> K3(0) -> Stop(1)
        sig_D    <= "0111";   -- Define o novo dado
        wait for 20 ns;
        sig_Load <= '1';      -- Dispara o Load
        wait for 40 ns;
        sig_Load <= '0';
        wait until sig_KBfree = '1';
        
        -- Espera terminar a segunda transmissão
        wait until sig_KBfree = '1';
        wait for 200 ns;

        -- Fim da simulação (para o processo não repetir indefinidamente)
        wait;
    end process;

end behavior;