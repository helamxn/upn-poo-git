from math import atan2, cos, sin, pi
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont


BASE_DIR = Path(__file__).resolve().parent
OUT_DIR = BASE_DIR / "capturas"
OUT_DIR.mkdir(parents=True, exist_ok=True)

FONT = "C:/Windows/Fonts/arial.ttf"
BOLD = "C:/Windows/Fonts/arialbd.ttf"


def font(size, bold=False):
    return ImageFont.truetype(BOLD if bold else FONT, size)


def wrap_text(draw, text, fnt, max_width):
    words = text.split()
    lines = []
    current = ""
    for word in words:
        test = f"{current} {word}".strip()
        width = draw.textbbox((0, 0), test, font=fnt)[2]
        if width <= max_width:
            current = test
        else:
            if current:
                lines.append(current)
            current = word
    if current:
        lines.append(current)
    return lines


def centered_text(draw, box, text, fnt, fill="#111111", max_width=None):
    x1, y1, x2, y2 = box
    lines = wrap_text(draw, text, fnt, max_width or (x2 - x1 - 24))
    line_height = fnt.size + 5
    total_height = line_height * len(lines)
    y = y1 + ((y2 - y1) - total_height) / 2
    for line in lines:
        bbox = draw.textbbox((0, 0), line, font=fnt)
        x = x1 + ((x2 - x1) - (bbox[2] - bbox[0])) / 2
        draw.text((x, y), line, font=fnt, fill=fill)
        y += line_height


def task(draw, box, text, fill="#F8FAFC", outline="#334155"):
    draw.rounded_rectangle(box, radius=18, fill=fill, outline=outline, width=3)
    centered_text(draw, box, text, font(28), max_width=box[2] - box[0] - 26)


def event(draw, center, text=None, fill="#DCFCE7"):
    x, y = center
    r = 28
    draw.ellipse((x - r, y - r, x + r, y + r), fill=fill, outline="#166534", width=4)
    if text:
        draw.text((x - 35, y + 36), text, font=font(22), fill="#111111")


def arrow(draw, start, end, fill="#111111", width=4):
    draw.line((start, end), fill=fill, width=width)
    angle = atan2(end[1] - start[1], end[0] - start[0])
    size = 16
    p1 = (
        end[0] - size * cos(angle - pi / 7),
        end[1] - size * sin(angle - pi / 7),
    )
    p2 = (
        end[0] - size * cos(angle + pi / 7),
        end[1] - size * sin(angle + pi / 7),
    )
    draw.polygon([end, p1, p2], fill=fill)


def make_canvas(title):
    img = Image.new("RGB", (1800, 900), "white")
    draw = ImageDraw.Draw(img)
    draw.text((520, 28), title, font=font(38, True), fill="#111111")
    return img, draw


def draw_lanes(draw, labels):
    top = 100
    lane_h = 230
    left = 70
    right = 1730
    for index, label in enumerate(labels):
        y1 = top + index * lane_h
        y2 = y1 + lane_h
        fill = "#F8FAFC" if index % 2 == 0 else "#FFFFFF"
        draw.rectangle((left, y1, right, y2), fill=fill, outline="#CBD5E1", width=2)
        draw.rectangle((left, y1, 240, y2), fill="#E2E8F0", outline="#94A3B8", width=2)
        centered_text(draw, (left, y1, 240, y2), label, font(26, True), fill="#0F172A")


def bpmn_as_is():
    img, draw = make_canvas("BPMN AS-IS - Proceso manual de ventas")
    draw_lanes(draw, ["Cliente", "Encargada", "Registro manual"])

    event(draw, (300, 215), "Inicio")
    task(draw, (390, 165, 650, 265), "Solicita productos")
    task(draw, (390, 395, 650, 495), "Busca productos fisicamente")
    task(draw, (720, 395, 980, 495), "Anota venta en cuaderno")
    task(draw, (1050, 395, 1310, 495), "Calcula total manualmente")
    task(draw, (1380, 395, 1640, 495), "Cobra al cliente")
    task(draw, (1050, 625, 1310, 725), "Guarda registro fisico")
    event(draw, (1430, 675), "Fin")

    arrow(draw, (328, 215), (390, 215))
    arrow(draw, (520, 265), (520, 395))
    arrow(draw, (650, 445), (720, 445))
    arrow(draw, (980, 445), (1050, 445))
    arrow(draw, (1310, 445), (1380, 445))
    arrow(draw, (1510, 495), (1180, 625))
    arrow(draw, (1310, 675), (1402, 675))

    draw.text((705, 815), "Problemas: calculo manual, datos dispersos y poca trazabilidad", font=font(26, True), fill="#B91C1C")
    img.save(OUT_DIR / "bpmn-as-is.png")


def bpmn_to_be():
    img, draw = make_canvas("BPMN TO-BE - Proceso con sistema")
    draw_lanes(draw, ["Cliente", "Encargada", "Sistema"])

    event(draw, (300, 215), "Inicio")
    task(draw, (390, 165, 650, 265), "Solicita productos")
    task(draw, (720, 395, 980, 495), "Busca o registra cliente")
    task(draw, (1050, 395, 1310, 495), "Selecciona productos y cantidades")
    task(draw, (720, 625, 980, 725), "Valida datos de cliente y producto")
    task(draw, (1050, 625, 1310, 725), "Calcula subtotal y total")
    task(draw, (1380, 625, 1640, 725), "Registra venta y permite consulta")
    event(draw, (1695, 675), "Fin")

    arrow(draw, (328, 215), (390, 215))
    arrow(draw, (650, 215), (720, 445))
    arrow(draw, (980, 445), (1050, 445))
    arrow(draw, (1180, 495), (850, 625))
    arrow(draw, (980, 675), (1050, 675))
    arrow(draw, (1310, 675), (1380, 675))
    arrow(draw, (1640, 675), (1667, 675))

    draw.text((705, 815), "Mejoras: validacion, calculo automatico, registro centralizado e historial", font=font(26, True), fill="#166534")
    img.save(OUT_DIR / "bpmn-to-be.png")


def navigation():
    img = Image.new("RGB", (1500, 900), "white")
    draw = ImageDraw.Draw(img)
    draw.text((435, 35), "Diagrama de navegacion del sistema", font=font(38, True), fill="#111111")

    menu = (575, 115, 925, 210)
    task(draw, menu, "Menu principal", fill="#DBEAFE", outline="#1D4ED8")

    boxes = {
        "Clientes": (115, 335, 435, 430),
        "Productos": (590, 335, 910, 430),
        "Ventas": (1065, 335, 1385, 430),
    }
    for label, box in boxes.items():
        task(draw, box, label, fill="#F8FAFC")
        arrow(draw, ((menu[0] + menu[2]) // 2, menu[3]), ((box[0] + box[2]) // 2, box[1]))

    details = {
        "Clientes": ["Registrar", "Listar", "Buscar"],
        "Productos": ["Registrar por tipo", "Listar", "Buscar", "Reporte por tipo"],
        "Ventas": ["Registrar venta", "Listar", "Ver detalle", "Ventas por cliente"],
    }
    for label, items in details.items():
        box = boxes[label]
        x1, y1, x2, _ = box
        y = 460
        for item in items:
            small = (x1, y, x2, y + 55)
            draw.rounded_rectangle(small, radius=14, fill="#FFFFFF", outline="#94A3B8", width=2)
            centered_text(draw, small, item, font(22), max_width=x2 - x1 - 20)
            y += 70

    draw.text((590, 815), "Opcion 0: salir del programa", font=font(26, True), fill="#334155")

    img.save(OUT_DIR / "diagrama-navegacion.png")


if __name__ == "__main__":
    bpmn_as_is()
    bpmn_to_be()
    navigation()
    print("Diagramas generados en", OUT_DIR)
