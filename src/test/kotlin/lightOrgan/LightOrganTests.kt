package lightOrgan

class LightOrganTests {

//    val inputManager: InputManager = InputManager()
//    val soundProcessor: SoundProcessor = SoundProcessor()
//    val colorProcessor: ColorProcessor = ColorProcessor()
//    val gatewayManager: GatewayManager = GatewayManager()
//
//    private val newAudio = nextAudioFrame()
//
//    private val subscriber1: LightOrganSubscriber = mockk(relaxed = true)
//    private val subscriber2: LightOrganSubscriber = mockk(relaxed = true)
//    private val subscribers = mutableSetOf(subscriber1, subscriber2)
//    private var colorFactory: ColorFactory = mockk()
//    private val nextColor = nextColor()
//
//    @BeforeEach
//    fun setupHappyPath() {
//        every { colorFactory.create(newAudio) } returns nextColor
//    }
//
//    @AfterEach
//    fun tearDown() {
//        clearAllMocks()
//    }
//
//    private fun createSUT(): LightOrgan {
//        return LightOrgan(
//            inputManager = inputManager,
//            soundProcessor = soundProcessor,
//            colorProcessor = colorProcessor,
//            gatewayManager = gatewayManager,
//        )
//    }
//
//    @Test
//    fun `send the next color to the subscribers when new audio is received`() {
//        val sut = createSUT()
//
//        sut.received(newAudio)
//
//        verify(exactly = 1) { subscriber1.new(nextColor) }
//        verify(exactly = 1) { subscriber2.new(nextColor) }
//    }
//
//    @Test
//    fun `add a subscriber`() {
//        val sut = createSUT()
//        val newSubscriber: LightOrganSubscriber = mockk()
//
//        sut.addSubscriber(newSubscriber)
//
//        assertTrue(subscribers.contains(newSubscriber))
//    }

}
