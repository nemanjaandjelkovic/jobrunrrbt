import Button from "@material-ui/core/Button";

export default function ActionButton(props) {
    const method = props.onClick
    const Icon = props.Icon;
    const Value = props.value;
    const Background = props.background;
    const Color = props.color;

    return (
        <Button variant="contained" style={{
            color: Color,
            backgroundColor: Background,
            margin: "5px"
        }} value={Value} onClick={() => method(Value)}
        >
            {Icon}
        </Button>
    );
}