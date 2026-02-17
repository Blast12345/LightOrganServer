package gui.dashboard.tiles.gateway

// TODO:
//class GatewayTileViewModelTests {
//
//    private val gatewayManager: GatewayManager = mockk(relaxed = true)
//    private val errorHandler: UIErrorHandler = mockk(relaxed = true)
//    private val testDispatcher = StandardTestDispatcher()
//    private val scope = TestScope(testDispatcher)
//
//    private val isSearchingFlow = MutableStateFlow(false)
//    private val currentGatewayFlow = MutableStateFlow<Gateway?>(null)
//
//    private val gateway: Gateway = mockk()
//    private val isConnectedFlow = MutableStateFlow(false)
//
//    @BeforeEach
//    fun setupHappyPath() {
//        every { gatewayManager.isSearching } returns isSearchingFlow
//        every { gatewayManager.currentGateway } returns currentGatewayFlow
//
//        every { gateway.isConnected } returns isConnectedFlow
//        every { gateway.systemPath } returns nextString("systemPath")
//        every { gateway.macAddress } returns nextString("macAddress")
//        every { gateway.firmwareVersion } returns nextString("firmwareVersion")
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @AfterEach
//    fun tearDown() {
//        clearAllMocks()
//    }
//
//    private fun createSUT(): GatewayTileViewModel {
//        return GatewayTileViewModel(
//            gatewayManager = gatewayManager,
//            errorHandler = errorHandler,
//            scope = scope,
//            ioDispatcher = testDispatcher
//        )
//    }
//
//    // Gateway Details
//    @Test
//    fun `given no gateway is available at init, then display empty connection details`() {
//        currentGatewayFlow.value = null
//
//        val sut = createSUT()
//
//        assertEquals("", sut.systemPath)
//        assertEquals("", sut.macAddress)
//        assertEquals("", sut.firmwareVersion)
//    }
//
//    @Test
//    fun `given a gateway is available at init, then display the gateways details`() {
//        currentGatewayFlow.value = gateway
//
//        val sut = createSUT()
//
//        assertEquals(gateway.systemPath, sut.systemPath)
//        assertEquals(gateway.macAddress, sut.macAddress)
//        assertEquals(gateway.firmwareVersion, sut.firmwareVersion)
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `when a gateway is found, then display gateway connection details`() = runTest {
//        currentGatewayFlow.value = null
//
//        val sut = createSUT()
//
//        currentGatewayFlow.value = gateway
//        scope.advanceUntilIdle()
//
//        assertEquals(gateway.systemPath, sut.systemPath)
//        assertEquals(gateway.macAddress, sut.macAddress)
//        assertEquals(gateway.firmwareVersion, sut.firmwareVersion)
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `given a gateway is lost, then display empty connection details`() = runTest {
//        currentGatewayFlow.value = gateway
//
//        val sut = createSUT()
//
//        currentGatewayFlow.value = null
//        scope.advanceUntilIdle()
//
//        assertEquals("", sut.systemPath)
//        assertEquals("", sut.macAddress)
//        assertEquals("", sut.firmwareVersion)
//    }
//
//    // Searching
//    @Test
//    fun `given a gateway search is not underway at init, then is searching is false`() {
//        isSearchingFlow.value = false
//
//        val sut = createSUT()
//
//        assertFalse(sut.isSearching)
//    }
//
//    @Test
//    fun `given a gateway search is underway at init, then is searching is true`() {
//        isSearchingFlow.value = true
//
//        val sut = createSUT()
//
//        assertTrue(sut.isSearching)
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `when a gateway search begins, then is searching is true`() = runTest {
//        val sut = createSUT()
//
//        isSearchingFlow.value = true
//        scope.advanceUntilIdle()
//
//        assertTrue(sut.isSearching)
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `when a gateway search ends, then is searching is false`() = runTest {
//        val sut = createSUT()
//
//        isSearchingFlow.value = true
//        scope.advanceUntilIdle()
//
//        isSearchingFlow.value = false
//        scope.advanceUntilIdle()
//
//        assertFalse(sut.isSearching)
//    }
//
//    // Connection state
//    @Test
//    fun `given a gateway exists but is disconnected at init, then the show connected`() {
//        currentGatewayFlow.value = gateway
//        isConnectedFlow.value = true
//
//        val sut = createSUT()
//
//        assertEquals(true, sut.isConnected)
//    }
//
//    @Test
//    fun `given a gateway exists but is disconnected at init, then show not connected`() {
//        currentGatewayFlow.value = gateway
//        isConnectedFlow.value = false
//
//        val sut = createSUT()
//
//        assertEquals(false, sut.isConnected)
//    }
//
//    @Test
//    fun `given no gateway exists at init, then show not connected`() {
//        currentGatewayFlow.value = null
//
//        val sut = createSUT()
//
//        assertEquals(false, sut.isConnected)
//    }
//
//    @Test
//    fun `when the connection state becomes true, then show connected`() = runTest {
//        val sut = createSUT()
//
//        currentGatewayFlow.value = gateway
//        isConnectedFlow.value = true
//        scope.testScheduler.advanceUntilIdle()
//
//        assertEquals(true, sut.isConnected)
//    }
//
//    @Test
//    fun `when the connection state becomes false, then show not connected`() = runTest {
//        val sut = createSUT()
//
//        currentGatewayFlow.value = gateway
//        isConnectedFlow.value = false
//        scope.testScheduler.advanceUntilIdle()
//
//        assertEquals(false, sut.isConnected)
//    }
//
//    // Connect
//    @Test
//    fun `connect to a gateway`() = runTest {
//        val sut = createSUT()
//
//        sut.connect()
//        scope.testScheduler.advanceUntilIdle()
//
//        coVerify { gatewayManager.connect() }
//    }
//
//    @Test
//    fun `when connecting to a gateway fails with an error message, then show the error`() = runTest {
//        val sut = createSUT()
//        val exception = nextException("findAndConnect")
//        coEvery { gatewayManager.connect() } throws exception
//
//        sut.connect()
//        scope.testScheduler.advanceUntilIdle()
//
//        coVerify { errorHandler.show(exception.message!!) }
//    }
//
//    @Test
//    fun `when connecting to a gateway fails without an error message, then show a generic error`() = runTest {
//        val sut = createSUT()
//        val exception = Exception()
//        coEvery { gatewayManager.connect() } throws exception
//
//        sut.connect()
//        scope.testScheduler.advanceUntilIdle()
//
//        coVerify { errorHandler.show("Failed to connect to gateway.") }
//    }
//
//    // Disconnect
//    @Test
//    fun `disconnect from a gateway`() = runTest {
//        val sut = createSUT()
//
//        sut.disconnect()
//        scope.testScheduler.advanceUntilIdle()
//
//        coVerify { gatewayManager.disconnect() }
//    }
//
//    @Test
//    fun `when disconnecting from a gateway fails with an error message, then show the error`() = runTest {
//        val sut = createSUT()
//        val exception = nextException("findAndConnect")
//        every { gatewayManager.disconnect() } throws exception
//
//        sut.disconnect()
//        scope.testScheduler.advanceUntilIdle()
//
//        coVerify { errorHandler.show(exception.message!!) }
//    }
//
//    @Test
//    fun `when disconnecting from a gateway fails without an error message, then show a generic error`() = runTest {
//        val sut = createSUT()
//        val exception = Exception()
//        every { gatewayManager.disconnect() } throws exception
//
//        sut.disconnect()
//        scope.testScheduler.advanceUntilIdle()
//
//        coVerify { errorHandler.show("Failed to disconnect from gateway.") }
//    }
//
//}