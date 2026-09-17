package com.example.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen()
                }
            }
        }
    }
}

/* ========== بيانات ثابتة ========== */

// ألوان جاهزة يختار منها المستخدم
val presetColors: List<Pair<String, Color>> = listOf(
    "أبيض"   to Color(0xFFFFFFFF),
    "أحمر"   to Color(0xFFE53935),
    "أصفر"   to Color(0xFFFFEB3B),
    "أزرق"   to Color(0xFF42A5F5),
    "أخضر"   to Color(0xFF43A047),
    "برتقالي" to Color(0xFFFB8C00),
    "بمبي"   to Color(0xFFEC407A),
    "أسود"   to Color(0xFF000000)
)

// خطوط جاهزة
data class FontChoice(val label: String, val family: FontFamily)

val presetFonts: List<FontChoice> = listOf(
    FontChoice("حديث",  FontFamily.SansSerif),
    FontChoice("نسخ",   FontFamily.Serif),
    FontChoice("مونو",  FontFamily.Monospace),
    FontChoice("مزخرف", FontFamily.Cursive)
)

// الكلام الثابت
val categories: Map<String, List<String>> = mapOf(
    "كلام تكاتك" to listOf(
        "خير أبويا",
        "عضو تسد ولا نظرة حسد",
        "الحمد لله على كل حال",
        "ما شاء الله ولا قوة إلا بالله",
        "لا حول ولا قوة إلا بالله",
        "ربنا يكرمك يا رب",
        "خليها على الله",
        "الله كريم",
        "الباقي على الله",
        "الحاسدين في هم",
        "إحنا ولا حد علينا",
        "ماحدش زينا والله",
        "علينا حاسدين كتير",
        "مفيش زي أخويا",
        "أمي هي حبيبتي",
        "بحبك يا مصر",
        "مصر أم الدنيا",
        "تحيا مصر",
        "دلع يا معلم",
        "سيبك من الكلام"
    ),
    "عامة" to listOf(
        "الدنيا علمتني إني ماشتكيش، وإن شكيت أشكي لربنا بس.",
        "ماتقولش ظروفي صعبة، قول الحمد لله على كل حال.",
        "اللي بيروح لحد ما يجيش، مش خسارة إنك تسيبه.",
        "مش كل اللي بيضحك مبسوط، ومش كل اللي ساكت زعلان.",
        "كون جدع، حتى لو الدنيا مش جدعة معاك.",
        "الصبر مفتاح الفرج، والله ما بينسى حد.",
        "القناعة كنز ما يفنى.",
        "ماتخليش حد يرسملك مستقبلك، إنت اللي ماسك القلم."
    ),
    "دينية" to listOf(
        "سبحان الله وبحمده، سبحان الله العظيم.",
        "الحمد لله على كل حال.",
        "لا حول ولا قوة إلا بالله.",
        "اللهم إني أسألك العفو والعافية.",
        "توكلت على الله، وهو حسبي ونعم الوكيل.",
        "أستغفر الله العظيم وأتوب إليه.",
        "اللهم إنك عفو تحب العفو فاعف عنا.",
        "الدنيا ساعة، اجعلها طاعة."
    ),
    "رومانسية" to listOf(
        "إنتي مش تفاصيل يومي، إنتي اليوم بحاله.",
        "قلبي مش محطة، بس إنت الوحيد اللي عجبه المكان.",
        "لو الدنيا وردة، إنتي ريحتها.",
        "بحبك بطريقة مش محتاجة شرح.",
        "القلب اللي بيحبش، مش بيبقى قلب.",
        "إنتي السبب إن ابتسامتي بقت صاحية بدري."
    ),
    "ضحك" to listOf(
        "أنا مش كسول، أنا في وضع توفير الطاقة.",
        "النوم مش عيب، العيب إنك تصحى بدري.",
        "أنا مش عصبي، أنا بس في بيئة غير مناسبة.",
        "بفكر أعمل رجيم، بس التفكير بيجيبلي جوع."
    )
)

/* ========== الشاشة الرئيسية ========== */

@Composable
fun HomeScreen() {
    val context = LocalContext.current

    val categoryNames = categories.keys.toList()
    var selectedCategory by remember { mutableIntStateOf(0) }

    // حالة الكارت العادي (للأقسام التانية)
    var statusIndex by remember { mutableIntStateOf(0) }

    // حالة التوكتوك
    var tuktukText by remember { mutableStateOf("خير أبويا") }
    var tuktukColor by remember { mutableStateOf(presetColors[0].second) } // أبيض افتراضي
    var tuktukFontIndex by remember { mutableIntStateOf(0) }

    val currentCatName = categoryNames[selectedCategory]
    val isTuktuk = currentCatName == "كلام تكاتك"

    val currentList = categories[currentCatName] ?: emptyList()
    val currentStatus = currentList[statusIndex.coerceIn(0, currentList.lastIndex)]

    // خانة للكتابة اللي بتظهر على التوكتوك
    val displayText = if (isTuktuk) tuktukText else currentStatus

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364),
                        Color(0xFF6A3093),
                        Color(0xFFA044FF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text(
                text = "✨ حالات وكلام",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
            )
            Text(
                text = "اختار فئتك وشاركها مع أصحابك",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // ---- التابات ----
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoryNames.forEachIndexed { i, name ->
                    val sel = i == selectedCategory
                    Surface(
                        shape = CircleShape,
                        color = if (sel) Color.White else Color.White.copy(alpha = 0.15f),
                        border = BorderStroke(
                            1.dp,
                            if (sel) Color.White.copy(alpha = 0.5f)
                            else Color.White.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.clickable {
                            selectedCategory = i
                            statusIndex = 0
                        }
                    ) {
                        Text(
                            text = name,
                            color = if (sel) Color(0xFF203A43) else Color.White,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---- العرض ----
            if (isTuktuk) {
                // التوكتوك
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .aspectRatio(1f)
                    ) {
                        val w = maxWidth
                        val h = maxHeight

                        // الرسم
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawTuktuk()
                        }

                        // الكلام فوق خانة الكتابة في التوكتوك
                        Box(
                            modifier = Modifier
                                .offset(x = w * 0.11f, y = h * 0.26f)
                                .size(w * 0.78f, h * 0.42f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tuktukText.ifBlank { "اكتب كلامك..." },
                                color = tuktukColor,
                                fontFamily = presetFonts[tuktukFontIndex].family,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 26.sp,
                                lineHeight = 34.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 4
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ---- مربع الكتابة ----
                OutlinedTextField(
                    value = tuktukText,
                    onValueChange = { tuktukText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("اكتب الكلام اللي على التوكتوك") },
                    placeholder = { Text("مثال: خير أبويا") },
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    singleLine = false,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White.copy(alpha = 0.7f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.35f),
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(14.dp))

                // ---- اختيار اللون ----
                Text(
                    text = "لون الكلام",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    presetColors.forEach { (name, color) ->
                        val selected = color == tuktukColor
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(color, CircleShape)
                                .border(
                                    width = if (selected) 3.dp else 1.dp,
                                    color = if (selected) Color.White else Color.White.copy(alpha = 0.4f),
                                    shape = CircleShape
                                )
                                .clickable { tuktukColor = color }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ---- اختيار الخط ----
                Text(
                    text = "الخط",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetFonts.forEachIndexed { i, font ->
                        val sel = i == tuktukFontIndex
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (sel) Color.White else Color.White.copy(alpha = 0.15f),
                            border = BorderStroke(
                                1.dp,
                                Color.White.copy(alpha = if (sel) 0.8f else 0.3f)
                            ),
                            modifier = Modifier.clickable { tuktukFontIndex = i }
                        ) {
                            Text(
                                text = "أبجد ${font.label}",
                                fontFamily = font.family,
                                color = if (sel) Color(0xFF203A43) else Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

            } else {
                // باقي الأقسام — كارت زجاجي عادي
                GlassCard(
                    text = currentStatus,
                    categoryName = currentCatName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )

                Spacer(Modifier.height(16.dp))
            }

            // ---- الأزرار ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        if (isTuktuk) {
                            // حالة جديدة من قائمة تكاتك
                            tuktukText = currentList.random()
                        } else {
                            statusIndex = (statusIndex + 1) % currentList.size
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "🔄 حالة جديدة",
                        color = Color(0xFF203A43),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, displayText)
                        }
                        context.startActivity(Intent.createChooser(intent, "شارك الحالة"))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA044FF))
                ) {
                    Text("📤 مشاركة", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("status", displayText))
                    Toast.makeText(context, "اتنسخ ✅", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("📋 نسخ الكلام", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

/* ========== رسم التوكتوك ========== */

fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTuktuk() {
    val w = size.width
    val h = size.height

    // --- السقف ---
    drawRoundRect(
        color = Color(0xFF263238),
        topLeft = Offset(w * 0.06f, h * 0.04f),
        size = Size(w * 0.88f, h * 0.11f),
        cornerRadius = CornerRadius(h * 0.03f, h * 0.03f)
    )
    // لمعة على السقف
    drawRoundRect(
        color = Color(0xFF455A64),
        topLeft = Offset(w * 0.10f, h * 0.05f),
        size = Size(w * 0.80f, h * 0.035f),
        cornerRadius = CornerRadius(h * 0.02f, h * 0.02f)
    )

    // --- الجسم العلوي (أصفر) ---
    drawRect(
        color = Color(0xFFFFC107),
        topLeft = Offset(w * 0.08f, h * 0.15f),
        size = Size(w * 0.84f, h * 0.09f)
    )

    // شريط أحمر
    drawRect(
        color = Color(0xFFD32F2F),
        topLeft = Offset(w * 0.08f, h * 0.20f),
        size = Size(w * 0.84f, h * 0.025f)
    )
    // شريط أزرق
    drawRect(
        color = Color(0xFF1565C0),
        topLeft = Offset(w * 0.08f, h * 0.225f),
        size = Size(w * 0.84f, h * 0.02f)
    )

    // --- خانة الكتابة (سطح غامق زي اللوح اللي بيتكتب عليه) ---
    drawRect(
        color = Color(0xFF1A1A1A),
        topLeft = Offset(w * 0.08f, h * 0.245f),
        size = Size(w * 0.84f, h * 0.45f)
    )
    // إطار داخلي رمادي
    drawRect(
        color = Color(0xFF5D4037),
        topLeft = Offset(w * 0.105f, h * 0.265f),
        size = Size(w * 0.79f, h * 0.41f),
        style = Stroke(width = 3f)
    )

    // --- الجسم السفلي (أصفر) ---
    drawRect(
        color = Color(0xFFFFC107),
        topLeft = Offset(w * 0.08f, h * 0.695f),
        size = Size(w * 0.84f, h * 0.12f)
    )

    // فوانيس خلفية (يمين وشمال)
    drawRoundRect(
        color = Color(0xFFD32F2F),
        topLeft = Offset(w * 0.10f, h * 0.72f),
        size = Size(w * 0.09f, h * 0.06f),
        cornerRadius = CornerRadius(h * 0.01f, h * 0.01f)
    )
    drawRoundRect(
        color = Color(0xFFD32F2F),
        topLeft = Offset(w * 0.81f, h * 0.72f),
        size = Size(w * 0.09f, h * 0.06f),
        cornerRadius = CornerRadius(h * 0.01f, h * 0.01f)
    )

    // لوحة أرقام
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(w * 0.38f, h * 0.73f),
        size = Size(w * 0.24f, h * 0.06f),
        cornerRadius = CornerRadius(h * 0.008f, h * 0.008f)
    )
    drawRect(
        color = Color(0xFF1565C0),
        topLeft = Offset(w * 0.38f, h * 0.73f),
        size = Size(w * 0.24f, h * 0.018f)
    )

    // --- العجلات ---
    drawCircle(
        color = Color(0xFF212121),
        radius = h * 0.075f,
        center = Offset(w * 0.22f, h * 0.90f)
    )
    drawCircle(
        color = Color(0xFF212121),
        radius = h * 0.075f,
        center = Offset(w * 0.78f, h * 0.90f)
    )
    // الطاسات
    drawCircle(
        color = Color(0xFF9E9E9E),
        radius = h * 0.03f,
        center = Offset(w * 0.22f, h * 0.90f)
    )
    drawCircle(
        color = Color(0xFF9E9E9E),
        radius = h * 0.03f,
        center = Offset(w * 0.78f, h * 0.90f)
    )

    // --- الحد الخارجي ---
    drawRect(
        color = Color(0xFF1A1A1A),
        topLeft = Offset(w * 0.08f, h * 0.15f),
        size = Size(w * 0.84f, h * 0.665f),
        style = Stroke(width = 4f)
    )
}

/* ========== الكارت الزجاجي (للأقسام التانية) ========== */

@Composable
fun GlassCard(
    text: String,
    categoryName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "❝",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 22.sp,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    text = "· $categoryName ·",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
        }
    }
}
