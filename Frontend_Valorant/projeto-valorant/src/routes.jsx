import {Navigate, Route, Routes} from "react-router-dom";
import Cadastro from "./pages/cadastro";
import Login from "./pages/login";
import Navbar from "./pages/navbar";
import Content from "./pages/content";
import Checkout from "./pages/checkout/Checkout.jsx";
import { Success } from './pages/feedback/Success';
import { Failure } from './pages/feedback/Failure';
import { Pending } from './pages/feedback/Pending';
import { ProtectRoutes } from './protectRoutes';

function AppRoutes() {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/login" />} />
            <Route path="/cadastro" element={<Cadastro/>}> </Route>
            <Route path="/login" element={<Login/>}> </Route>
            <Route path="/navbar" element={<Navbar/>}> </Route>
            <Route path="/checkout" element={<Checkout />} />
            <Route path="/checkout/success" element={<Success />} />
            <Route path="/checkout/failure" element={<Failure />} />
            <Route path="/checkout/pending" element={<Pending />} />
            <Route element={<ProtectRoutes />}>
                <Route path="/content" element={<Content />} />
            </Route>
        </Routes>
    )
}

export default AppRoutes